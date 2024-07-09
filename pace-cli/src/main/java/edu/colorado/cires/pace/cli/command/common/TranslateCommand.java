package edu.colorado.cires.pace.cli.command.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.translator.TranslatorRepositoryFactory;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.translator.Translator;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.translator.CSVReader;
import edu.colorado.cires.pace.translator.ExcelReader;
import edu.colorado.cires.pace.translator.ObjectWithRowException;
import edu.colorado.cires.pace.translator.SpreadsheetConverter;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.translator.TranslatorValidationException;
import edu.colorado.cires.pace.translator.converter.Converter;
import edu.colorado.cires.pace.utilities.ApplicationPropertyResolver;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import edu.colorado.cires.pace.utilities.TranslationType;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class TranslateCommand<O extends ObjectWithUniqueField, T extends Translator> implements Runnable {

  protected abstract Supplier<TranslationType> getTranslationTypeSupplier();
  protected abstract Supplier<String> getTranslatorNameSupplier();
  protected abstract Supplier<File> getInputSupplier();
  
  protected final ObjectMapper objectMapper = SerializationUtils.createObjectMapper();
  protected final Path workDir = new ApplicationPropertyResolver().getDataDir();
  
  protected abstract Converter<T, O> getConverter() throws IOException;
  
  protected abstract TypeReference<List<O>> getTypeReference();

  @Override
  public void run() {
    try {
      TranslationType translationType = getTranslationTypeSupplier().get();
      String translatorName = getTranslatorNameSupplier().get();
      File inputFile = getInputSupplier().get();
      List<O> translations = new ArrayList<>(0);
      switch (translationType) {
        case csv -> translations = translateCSV(translatorName, inputFile);
        case excel -> translations = translateExcel(translatorName, inputFile);
      }

      System.out.println(
          objectMapper.writerFor(getTypeReference())
            .withDefaultPrettyPrinter()
            .writeValueAsString(translations)
      );

    } catch (NotFoundException | IOException | DatastoreException | TranslatorValidationException e) {
      throw new RuntimeException(e);
    }
  }
  
  private List<O> translateCSV(String translatorName, File inputFile)
      throws IOException, NotFoundException, DatastoreException, TranslatorValidationException {
    try (InputStream inputStream = new FileInputStream(inputFile); Reader reader = new InputStreamReader(inputStream)) {
      Stream<ObjectWithRowException<O>> translated = SpreadsheetConverter.execute(
          () -> {
            try {
              return CSVReader.read(reader);
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          },
          getTranslator(translatorName),
          getConverter()
      );
      
      return postProcessTranslation(translated);
    }
  }
  
  private List<O> translateExcel(String translatorName, File inputFile)
      throws IOException, NotFoundException, DatastoreException, TranslatorValidationException {
    try (InputStream inputStream = new FileInputStream(inputFile)) {
      Stream<ObjectWithRowException<O>> translated = SpreadsheetConverter.execute(
          () -> {
            try {
              return ExcelReader.read(inputStream, 0);
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          },
          getTranslator(translatorName),
          getConverter()
      );
      
      return postProcessTranslation(translated);
    }
  }
  
  private List<O> postProcessTranslation(Stream<ObjectWithRowException<O>> translated) throws TranslationException {
    List<ObjectWithRowException<O>> results = translated.toList();
    TranslationException exception = (TranslationException) results.stream()
        .map(ObjectWithRowException::throwable)
        .filter(Objects::nonNull)
        .reduce(new TranslationException("Translation failed"), (rte1, rte2) -> {
          for (Throwable throwable : rte2.getSuppressed()) {
            rte1.addSuppressed(throwable);
          }
          return rte1;
        });
    
    if (exception.getSuppressed().length > 0) {
      throw exception;
    }
    
    return results.stream()
        .map(ObjectWithRowException::object)
        .toList();
  }
  
  private T getTranslator(String translatorName) throws IOException, NotFoundException, DatastoreException {
    TranslatorRepository repository = TranslatorRepositoryFactory.createRepository(
        workDir, objectMapper
    );
    try {
      return (T) TranslatorRepositoryFactory.createRepository(
        workDir, objectMapper
    ).getByUniqueField(translatorName);
    } catch (ClassCastException e) {
      throw new NotFoundException(String.format(
          "Translator %s not applicable to %s objects", translatorName, repository.getClassName()
      ));
    }
  }
}

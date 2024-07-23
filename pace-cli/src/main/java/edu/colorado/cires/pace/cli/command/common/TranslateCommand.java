package edu.colorado.cires.pace.cli.command.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.translator.TranslatorRepositoryFactory;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.translator.Translator;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.translator.CSVReader;
import edu.colorado.cires.pace.translator.ExcelReader;
import edu.colorado.cires.pace.translator.ObjectWithRowError;
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
import java.nio.charset.StandardCharsets;
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
  protected final Path workDir = ApplicationPropertyResolver.getDataDir();
  
  protected abstract Converter<T, O> getConverter() throws IOException;
  
  protected abstract TypeReference<List<O>> getTypeReference();

  @Override
  public void run() {
    String translatorName = getTranslatorNameSupplier().get();
    try {
      TranslationType translationType = getTranslationTypeSupplier().get();
      File inputFile = getInputSupplier().get();
      List<O> translations = new ArrayList<>(0);
      T translator = getTranslator(translatorName);
      
      switch (translationType) {
        case csv -> translations = translateCSV(translator, inputFile);
        case excel -> translations = translateExcel(translator, inputFile);
      }

      System.out.println(
          objectMapper.writerFor(getTypeReference())
            .withDefaultPrettyPrinter()
            .writeValueAsString(translations)
      );

    } catch (NotFoundException | IOException | DatastoreException | TranslatorValidationException e) {
      throw new RuntimeException(e);
    } catch (ClassCastException e) {
      throw new RuntimeException(String.format(
          "Translator with name = %s is not applicable to %s objects", translatorName, getClazz().getSimpleName()
      ));
    }
  }

  protected abstract Class<O> getClazz();

  private List<O> translateCSV(T translator, File inputFile)
      throws IOException, NotFoundException, DatastoreException, TranslatorValidationException {
    try (InputStream inputStream = new FileInputStream(inputFile); Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
      Stream<ObjectWithRowError<O>> translated = SpreadsheetConverter.execute(
          () -> {
            try {
              return CSVReader.read(reader);
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          },
          translator,
          getConverter()
      );
      
      return postProcessTranslation(translated);
    }
  }
  
  private List<O> translateExcel(T translator, File inputFile)
      throws IOException, NotFoundException, DatastoreException, TranslatorValidationException {
    try (InputStream inputStream = new FileInputStream(inputFile)) {
      Stream<ObjectWithRowError<O>> translated = SpreadsheetConverter.execute(
          () -> {
            try {
              return ExcelReader.read(inputStream, 0);
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          },
          translator,
          getConverter()
      );
      
      return postProcessTranslation(translated);
    }
  }
  
  private List<O> postProcessTranslation(Stream<ObjectWithRowError<O>> translated) throws TranslationException {
    List<ObjectWithRowError<O>> results = translated.toList();
    TranslationException exception = (TranslationException) results.stream()
        .map(ObjectWithRowError::throwable)
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
        .map(ObjectWithRowError::object)
        .toList();
  }
  
  private T getTranslator(String translatorName) throws IOException, NotFoundException, DatastoreException {
    return (T) TranslatorRepositoryFactory.createRepository(
        workDir, objectMapper
    ).getByUniqueField(translatorName);
  }
}

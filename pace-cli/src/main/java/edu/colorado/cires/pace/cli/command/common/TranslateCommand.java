package edu.colorado.cires.pace.cli.command.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.translation.TranslatorRepositoryFactory;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.data.translator.Translator;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.translator.CSVReader;
import edu.colorado.cires.pace.translator.ExcelReader;
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
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class TranslateCommand<O extends ObjectWithUniqueField, T extends Translator> implements Runnable {

  protected abstract Supplier<TranslationType> getTranslationTypeSupplier();
  protected abstract Supplier<String> getTranslatorNameSupplier();
  protected abstract Supplier<File> getInputSupplier();
  protected abstract Class<O> getJsonClass();
  
  protected final ObjectMapper objectMapper = SerializationUtils.createObjectMapper();
  protected final Path workDir = new ApplicationPropertyResolver().getDataDir();
  
  protected abstract RepositoryFactory<O>[] getDependencyRepositoryFactories();
  protected abstract Converter<T, O> getConverter() throws IOException; 
  
  protected CRUDRepository<?>[] getDependencyRepositories() {
    return Arrays.stream(getDependencyRepositoryFactories())
        .map(f -> {
          try {
            return f.createRepository(workDir, objectMapper);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }).toArray(CRUDRepository<?>[]::new);
  }

  @Override
  public void run() {
    try {
      TranslationType translationType = getTranslationTypeSupplier().get();
      String translatorName = getTranslatorNameSupplier().get();
      File inputFile = getInputSupplier().get();
      List<O> translations;
      switch (translationType) {
        case csv -> translations = translateCSV(translatorName, inputFile);
        case excel -> translations = translateExcel(translatorName, inputFile);
        default -> throw new IllegalArgumentException(String.format(
            "Translation type not supported: %s", translationType
        ));
      }

      System.out.println(
          objectMapper.writerFor(new TypeReference<List<Package>>() {})
            .withDefaultPrettyPrinter()
            .writeValueAsString(translations)
      );

    } catch (NotFoundException | IOException | DatastoreException | TranslatorValidationException e) {
      throw new RuntimeException(e);
    }
  }
  
  private List<O> translateCSV(String translatorName, File inputFile)
      throws IOException, NotFoundException, DatastoreException, TranslatorValidationException {
    try (
        InputStream inputStream = new FileInputStream(inputFile);
        Reader reader = new InputStreamReader(inputStream)
    ) {
      Stream<O> translated = SpreadsheetConverter.execute(
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
      Stream<O> translated = SpreadsheetConverter.execute(
          () -> {
            try {
              return ExcelReader.read(inputStream, 1);
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
  
  private List<O> postProcessTranslation(Stream<O> translated) throws TranslationException {
    return translated.toList();
  }
  
  private T getTranslator(String translatorName) throws IOException, NotFoundException, DatastoreException {
    try {
      return (T) TranslatorRepositoryFactory.createRepository(
        workDir, objectMapper
    ).getByUniqueField(translatorName);
    } catch (ClassCastException e) {
      throw new NotFoundException(String.format(
          "Translator %s not applicable to %s objects", translatorName, getJsonClass().getSimpleName()
      ));
    }
  }
}

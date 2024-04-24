package edu.colorado.cires.pace.cli.command.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.translation.csv.CSVTranslatorRepositoryFactory;
import edu.colorado.cires.pace.cli.command.translation.excel.ExcelTranslatorRepositoryFactory;
import edu.colorado.cires.pace.cli.util.ApplicationPropertyResolver;
import edu.colorado.cires.pace.cli.util.SerializationUtils;
import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.translator.ObjectWithRuntimeException;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.translator.csv.CSVTranslatorExecutor;
import edu.colorado.cires.pace.translator.excel.ExcelTranslatorExecutor;
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

abstract class TranslateCommand<O extends ObjectWithUniqueField> implements Runnable {
  
  protected abstract Supplier<TranslationType> getTranslationTypeSupplier();
  protected abstract Supplier<String> getTranslatorNameSupplier();
  protected abstract Supplier<File> getInputSupplier();
  protected abstract <T> Class<T> getJsonClass();
  
  private final ObjectMapper objectMapper = SerializationUtils.createObjectMapper();
  private final Path workDir = new ApplicationPropertyResolver().getWorkDir();

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

      System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
          translations
      ));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    } catch (NotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (DatastoreException e) {
      throw new RuntimeException(e);
    }
  }
  
  private List<O> translateCSV(String translatorName, File inputFile) throws IOException, NotFoundException, DatastoreException {
    try (
        InputStream inputStream = new FileInputStream(inputFile);
        Reader reader = new InputStreamReader(inputStream)
    ) {
      Stream<ObjectWithRuntimeException<Object>> translated = new CSVTranslatorExecutor<>(
          getCSVTranslator(translatorName),
          getJsonClass()
      ).translate(reader);
      
      return postProcessTranslation(translated);
    }
  }
  
  private List<O> translateExcel(String translatorName, File inputFile) throws IOException, NotFoundException, DatastoreException {
    try (InputStream inputStream = new FileInputStream(inputFile)) {
      Stream<ObjectWithRuntimeException<Object>> translated = new ExcelTranslatorExecutor<>(
          getExcelTranslator(translatorName),
          getJsonClass()
      ).translate(inputStream);
      
      return postProcessTranslation(translated);
    }
  }
  
  private List<O> postProcessTranslation(Stream<ObjectWithRuntimeException<Object>> translated) throws TranslationException {
    List<O> objects = new ArrayList<>(0);
        
    RuntimeException runtimeException = translated
        .peek(o -> objects.add((O) o.object()))
        .map(ObjectWithRuntimeException::runtimeException)
        .filter(Objects::nonNull)
        .reduce(new RuntimeException("Translation failed"), (o1, o2) -> {
          for (Throwable throwable : o2.getSuppressed()) {
            o1.addSuppressed(throwable);
          }
          return o1;
        });
    
    if (runtimeException.getSuppressed().length != 0) {
      
      TranslationException translationException = new TranslationException("Translation failed");
      for (Throwable throwable : runtimeException.getSuppressed()) {
        translationException.addSuppressed(throwable);
      }
      throw translationException;
    }
            
    return objects;
  }
  
  private ExcelTranslator getExcelTranslator(String translatorName) throws IOException, NotFoundException, DatastoreException {
    return ExcelTranslatorRepositoryFactory.createRepository(
        workDir, objectMapper
    ).getByUniqueField(translatorName);
  }
  
  private CSVTranslator getCSVTranslator(String translatorName) throws IOException, NotFoundException, DatastoreException {
    return CSVTranslatorRepositoryFactory.createRepository(
        workDir, objectMapper
    ).getByUniqueField(translatorName);
  }
}

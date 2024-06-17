package edu.colorado.cires.pace.cli.command.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.translation.csv.CSVTranslatorRepositoryFactory;
import edu.colorado.cires.pace.cli.command.translation.excel.ExcelTranslatorRepositoryFactory;
import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.translator.ObjectWithRowException;
import edu.colorado.cires.pace.translator.RowConversionException;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.translator.TranslatorValidationException;
import edu.colorado.cires.pace.translator.csv.CSVTranslatorExecutor;
import edu.colorado.cires.pace.translator.excel.ExcelTranslatorExecutor;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class TranslateCommand<O> implements Runnable {

  protected abstract Supplier<TranslationType> getTranslationTypeSupplier();
  protected abstract Supplier<String> getTranslatorNameSupplier();
  protected abstract Supplier<File> getInputSupplier();
  protected abstract <T> Class<T> getJsonClass();
  
  private final ObjectMapper objectMapper = SerializationUtils.createObjectMapper();
  private final Path workDir = new ApplicationPropertyResolver().getDataDir();
  
  protected abstract RepositoryFactory[] getDependencyRepositoryFactories();
  
  private CRUDRepository<?>[] getDependencyRepositories() {
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

    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    } catch (NotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (DatastoreException e) {
      throw new RuntimeException(e);
    } catch (TranslatorValidationException e) {
      throw new RuntimeException(e);
    }
  }
  
  private List<O> translateCSV(String translatorName, File inputFile)
      throws IOException, NotFoundException, DatastoreException, TranslatorValidationException {
    try (
        InputStream inputStream = new FileInputStream(inputFile);
        Reader reader = new InputStreamReader(inputStream)
    ) {
      Stream<ObjectWithRowException<Object>> translated = new CSVTranslatorExecutor<>(
          getCSVTranslator(translatorName),
          getJsonClass(),
          getDependencyRepositories()
      ).translate(reader);
      
      return postProcessTranslation(translated);
    }
  }
  
  private List<O> translateExcel(String translatorName, File inputFile)
      throws IOException, NotFoundException, DatastoreException, TranslatorValidationException {
    try (InputStream inputStream = new FileInputStream(inputFile)) {
      Stream<ObjectWithRowException<Object>> translated = new ExcelTranslatorExecutor<>(
          getExcelTranslator(translatorName),
          getJsonClass(),
          getDependencyRepositories()
      ).translate(inputStream);
      
      return postProcessTranslation(translated);
    }
  }
  
  private List<O> postProcessTranslation(Stream<ObjectWithRowException<Object>> translated) throws TranslationException {
    List<O> objects = new ArrayList<>(0);
        
    List<Throwable> exceptions = translated
        .peek(o -> objects.add((O) o.object()))
        .map(ObjectWithRowException::throwable)
        .filter(Objects::nonNull)
        .toList();
    
    if (!exceptions.isEmpty()) {
      
      TranslationException translationException = new TranslationException("Translation failed");
      for (Throwable rowConversionException : exceptions) {
        translationException.addSuppressed(rowConversionException);
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

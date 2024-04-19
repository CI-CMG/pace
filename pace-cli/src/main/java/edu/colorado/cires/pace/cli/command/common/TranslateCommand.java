package edu.colorado.cires.pace.cli.command.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.translation.csv.CSVTranslatorRepositoryFactory;
import edu.colorado.cires.pace.cli.command.translation.excel.ExcelTranslatorRepositoryFactory;
import edu.colorado.cires.pace.cli.util.ApplicationPropertyResolver;
import edu.colorado.cires.pace.cli.util.SerializationUtils;
import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.translator.csv.CSVTranslatorExecutor;
import edu.colorado.cires.pace.translator.excel.ExcelTranslatorExecutor;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.util.List;
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
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  private List<O> translateCSV(String translatorName, File inputFile) {
    try (
        InputStream inputStream = new FileInputStream(inputFile);
        Reader reader = new InputStreamReader(inputStream)
    ) {
      Stream<Object> translated = new CSVTranslatorExecutor<>(
          getCSVTranslator(translatorName),
          getJsonClass()
      ).translate(reader);
      
      return postProcessTranslation(translated);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  private List<O> translateExcel(String translatorName, File inputFile) {
    try (InputStream inputStream = new FileInputStream(inputFile)) {
      Stream<Object> translated = new ExcelTranslatorExecutor<>(
          getExcelTranslator(translatorName),
          getJsonClass()
      ).translate(inputStream);
      
      return postProcessTranslation(translated);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  private List<O> postProcessTranslation(Stream<Object> translated) {
    return translated
        .map(o -> (O) o)
        .toList();
  }
  
  private ExcelTranslator getExcelTranslator(String translatorName) throws Exception {
    return ExcelTranslatorRepositoryFactory.createRepository(
        workDir, objectMapper
    ).getByUniqueField(translatorName);
  }
  
  private CSVTranslator getCSVTranslator(String translatorName) throws Exception {
    return CSVTranslatorRepositoryFactory.createRepository(
        workDir, objectMapper
    ).getByUniqueField(translatorName);
  }
}

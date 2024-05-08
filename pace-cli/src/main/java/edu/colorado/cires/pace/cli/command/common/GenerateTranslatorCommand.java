package edu.colorado.cires.pace.cli.command.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.translator.FieldNameFactory;
import edu.colorado.cires.pace.translator.csv.CSVTranslatorFactory;
import edu.colorado.cires.pace.translator.excel.ExcelTranslatorFactory;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import edu.colorado.cires.pace.utilities.TranslationType;
import java.util.List;

public abstract class GenerateTranslatorCommand<O> implements Runnable {
  
  private final ObjectMapper objectMapper = SerializationUtils.createObjectMapper();
  
  protected abstract Class<O> getClazz();
  
  protected List<String> getFieldNames() {
    return FieldNameFactory.getDefaultDeclaredFields(getClazz());
  }
  
  protected String getTranslatorName() {
    return getClazz().getSimpleName();
  }
  
  private CSVTranslator getCSVTranslator() {
    return CSVTranslatorFactory.createTranslator(getTranslatorName(), this::getFieldNames);
  }
  
  private ExcelTranslator getExcelTranslator() {
    return ExcelTranslatorFactory.createTranslator(getTranslatorName(), this::getFieldNames);
  }
  
  protected abstract TranslationType getTranslatorType();

  @Override
  public void run() {
    try {
      System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
          getTranslatorType().equals(TranslationType.csv) ? getCSVTranslator() : getExcelTranslator()
      ));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

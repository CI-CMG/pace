package edu.colorado.cires.pace.cli.command.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.data.object.CSVTranslatorField;
import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.object.ExcelTranslatorField;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import edu.colorado.cires.pace.utilities.TranslationType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GenerateTranslatorCommand<O> implements Runnable {
  
  private final ObjectMapper objectMapper = SerializationUtils.createObjectMapper();
  
  protected abstract Class<O> getClazz();
  
  protected List<String> getFieldNames() throws Exception {
    return Arrays.stream(getClazz().getDeclaredFields())
        .map(Field::getName)
        .toList();
  }
  
  protected String getTranslatorName() {
    return getClazz().getSimpleName();
  }
  
  private CSVTranslator getCSVTranslator() throws Exception {
    List<CSVTranslatorField> fields = new ArrayList<>(0);
    
    List<String> objectFields = getFieldNames();
    for (int i = 0; i < objectFields.size(); i++) {
      fields.add(CSVTranslatorField.builder()
              .propertyName(objectFields.get(i))
              .columnNumber(i + 1)
          .build());
    }
    
    return CSVTranslator.builder()
        .name(getTranslatorName())
        .fields(fields)
        .build();
  }
  
  private ExcelTranslator getExcelTranslator() throws Exception {
    List<ExcelTranslatorField> fields = new ArrayList<>(0);

    List<String> objectFields = getFieldNames();
    for (int i = 0; i < objectFields.size(); i++) {
      fields.add(ExcelTranslatorField.builder()
              .propertyName(objectFields.get(i))
              .columnNumber(i + 1)
              .sheetNumber(1)
          .build());
    }
    
    return ExcelTranslator.builder()
        .name(getTranslatorName())
        .fields(fields)
        .build();
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

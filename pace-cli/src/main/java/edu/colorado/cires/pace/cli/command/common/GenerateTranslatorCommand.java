package edu.colorado.cires.pace.cli.command.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.util.SerializationUtils;
import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.data.object.CSVTranslatorField;
import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.object.ExcelTranslatorField;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class GenerateTranslatorCommand<O> implements Runnable {
  
  private final ObjectMapper objectMapper = SerializationUtils.createObjectMapper();
  
  protected abstract Class<O> getClazz();
  
  protected CSVTranslator getCSVTranslator() {
    List<CSVTranslatorField> fields = new ArrayList<>(0);
    
    Field[] objectFields = getClazz().getDeclaredFields();
    for (int i = 0; i < objectFields.length; i++) {
      fields.add(CSVTranslatorField.builder()
              .propertyName(objectFields[i].getName())
              .columnNumber(i + 1)
          .build());
    }
    
    return CSVTranslator.builder()
        .name(getClazz().getSimpleName())
        .fields(fields)
        .build();
  }
  
  protected ExcelTranslator getExcelTranslator() {
    List<ExcelTranslatorField> fields = new ArrayList<>(0);

    Field[] objectFields = getClazz().getDeclaredFields();
    for (int i = 0; i < objectFields.length; i++) {
      fields.add(ExcelTranslatorField.builder()
              .propertyName(objectFields[i].getName())
              .columnNumber(i + 1)
              .sheetNumber(1)
          .build());
    }
    
    return ExcelTranslator.builder()
        .name(getClazz().getSimpleName())
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
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}

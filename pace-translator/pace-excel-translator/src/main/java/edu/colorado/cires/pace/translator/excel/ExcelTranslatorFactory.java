package edu.colorado.cires.pace.translator.excel;

import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.object.ExcelTranslatorField;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class ExcelTranslatorFactory {
  
  public static ExcelTranslator createTranslator(String translatorName, Supplier<List<String>> fieldNameSupplier) {
    List<ExcelTranslatorField> fields = new ArrayList<>(0);

    List<String> objectFields = fieldNameSupplier.get();
    for (int i = 0; i < objectFields.size(); i++) {
      fields.add(ExcelTranslatorField.builder()
          .propertyName(objectFields.get(i))
          .columnNumber(i + 1)
          .sheetNumber(1)
          .build());
    }

    return ExcelTranslator.builder()
        .name(translatorName)
        .fields(fields)
        .build();
  }

}

package edu.colorado.cires.pace.translator.csv;

import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.data.object.CSVTranslatorField;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class CSVTranslatorFactory {
  
  public static CSVTranslator createTranslator(String translatorName, Supplier<List<String>> fieldNameSupplier) {
    List<CSVTranslatorField> fields = new ArrayList<>(0);

    List<String> objectFields = fieldNameSupplier.get();
    for (int i = 0; i < objectFields.size(); i++) {
      fields.add(CSVTranslatorField.builder()
          .propertyName(objectFields.get(i))
          .columnNumber(i + 1)
          .build());
    }

    return CSVTranslator.builder()
        .name(translatorName)
        .fields(fields)
        .build();
  }

}

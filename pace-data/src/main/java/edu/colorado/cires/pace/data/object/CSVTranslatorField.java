package edu.colorado.cires.pace.data.object;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class CSVTranslatorField implements TabularTranslationField {
  final String propertyName;
  final Integer columnNumber;

  CSVTranslatorField(String propertyName, Integer columnNumber) {
    this.propertyName = propertyName;
    this.columnNumber = columnNumber;
  }
}

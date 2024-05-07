package edu.colorado.cires.pace.data.object;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
public class ExcelTranslatorField implements TabularTranslationField {
  final String propertyName;
  final Integer columnNumber;
  final Integer sheetNumber;

  @Builder(toBuilder = true)
  @Jacksonized
  public ExcelTranslatorField(String propertyName, Integer columnNumber, Integer sheetNumber) {
    this.propertyName = propertyName;
    this.columnNumber = columnNumber;
    this.sheetNumber = sheetNumber;
  }
}

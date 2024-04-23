package edu.colorado.cires.pace.data.object;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
public class ExcelTranslatorField implements TabularTranslationField {
  final String propertyName;
  final int columnNumber;
  final int sheetNumber;

  @Builder(toBuilder = true)
  @Jacksonized
  public ExcelTranslatorField(String propertyName, int columnNumber, int sheetNumber) {
    this.propertyName = propertyName;
    this.columnNumber = columnNumber;
    this.sheetNumber = sheetNumber;
  }
}

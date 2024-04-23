package edu.colorado.cires.pace.data.object;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
public class ExcelTranslatorFieldImpl implements ExcelTranslatorField {
  final String propertyName;
  final int columnNumber;
  final int sheetNumber;
  final boolean multiValued;

  @Builder(toBuilder = true)
  @Jacksonized
  public ExcelTranslatorFieldImpl(String propertyName, int columnNumber, int sheetNumber, Boolean multiValued) {
    this.propertyName = propertyName;
    this.columnNumber = columnNumber;
    this.sheetNumber = sheetNumber;
    this.multiValued = multiValued != null && multiValued;
  }
}

package edu.colorado.cires.pace.data.object;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class ExcelResourceTranslatorField implements ExcelTranslatorField, ResourceTranslationField {
  final String propertyName;
  final int columnNumber;
  final int sheetNumber;
  final boolean multiValued;
  final String resourceName;

  public ExcelResourceTranslatorField(String propertyName, int columnNumber, int sheetNumber, Boolean multiValued, String resourceName) {
    this.propertyName = propertyName;
    this.columnNumber = columnNumber;
    this.sheetNumber = sheetNumber;
    this.multiValued = multiValued != null && multiValued;
    this.resourceName = resourceName;
  }
}

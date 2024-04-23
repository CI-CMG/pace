package edu.colorado.cires.pace.data.object;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class CSVResourceTranslatorField implements CSVTranslatorField, ResourceTranslationField {
  final String propertyName;
  final int columnNumber;
  final boolean multiValued;
  final String resourceName;

  public CSVResourceTranslatorField(String propertyName, int columnNumber, Boolean multiValued, String resourceName) {
    this.propertyName = propertyName;
    this.columnNumber = columnNumber;
    this.multiValued = multiValued != null && multiValued;
    this.resourceName = resourceName;
  }
}

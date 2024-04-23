package edu.colorado.cires.pace.data.object;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class CSVTranslatorFieldImpl implements CSVTranslatorField {
  final String propertyName;
  final int columnNumber;
  final boolean multiValued;

  CSVTranslatorFieldImpl(String propertyName, int columnNumber, Boolean multiValued) {
    this.propertyName = propertyName;
    this.columnNumber = columnNumber;
    this.multiValued = multiValued != null && multiValued;
  }
}

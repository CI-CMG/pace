package edu.colorado.cires.pace.data.object;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class CSVTranslatorField implements TabularTranslationField {
  final String propertyName;
  final int columnNumber;
}

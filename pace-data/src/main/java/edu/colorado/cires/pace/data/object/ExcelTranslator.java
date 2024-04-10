package edu.colorado.cires.pace.data.object;

import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class ExcelTranslator implements TabularTranslator<ExcelTranslatorField> {
  
  final UUID uuid;
  final String name;
  final List<ExcelTranslatorField> fields;

}

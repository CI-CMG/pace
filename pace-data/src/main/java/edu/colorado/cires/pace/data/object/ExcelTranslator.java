package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.validation.ExcelTranslatorValidator;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
public class ExcelTranslator implements TabularTranslator<ExcelTranslatorField> {
  
  final UUID uuid;
  final String name;
  final List<ExcelTranslatorField> fields;

  @Builder(toBuilder = true)
  @Jacksonized
  private ExcelTranslator(UUID uuid, String name, List<ExcelTranslatorField> fields) throws ValidationException {
    this.uuid = uuid;
    this.name = name;
    this.fields = fields;
    
    new ExcelTranslatorValidator().validate(this);
  }
}

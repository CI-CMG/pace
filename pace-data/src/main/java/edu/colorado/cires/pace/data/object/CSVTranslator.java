package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.validation.CSVTranslatorValidator;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
public class CSVTranslator implements TabularTranslator<CSVTranslatorField> {
  
  private final UUID uuid;
  private final String name;
  private final List<CSVTranslatorField> fields;

  @Builder(toBuilder = true)
  @Jacksonized
  private CSVTranslator(UUID uuid, String name, List<CSVTranslatorField> fields) throws ValidationException {
    this.uuid = uuid;
    this.name = name;
    this.fields = fields;
    
    new CSVTranslatorValidator().validate(this);
  }
}

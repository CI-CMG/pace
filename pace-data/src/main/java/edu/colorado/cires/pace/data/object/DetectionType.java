package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.validation.DetectionTypeValidator;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
public class DetectionType implements ObjectWithUniqueField {

  final UUID uuid;
  final String source;
  final String scienceName;

  @Builder(toBuilder = true)
  @Jacksonized
  private DetectionType(UUID uuid, String source, String scienceName) throws ValidationException {
    this.uuid = uuid;
    this.source = source;
    this.scienceName = scienceName;
    
    new DetectionTypeValidator().validate(this);
  }

}

package edu.colorado.cires.pace.data.object;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class DetectionType implements ObjectWithUniqueField {

  final UUID uuid;
  final String source;
  final String scienceName;

  @Override
  public String getUniqueField() {
    return scienceName;
  }

}

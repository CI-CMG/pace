package edu.colorado.cires.pace.data.object;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class DetectionType implements ObjectWithUniqueField {

  private final UUID uuid;
  @NotBlank
  private final String source;
  private final String scienceName;

}

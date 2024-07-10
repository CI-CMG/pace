package edu.colorado.cires.pace.data.object;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
  @Size(min = 1, message = "must not be blank")
  private final String scienceName;

}

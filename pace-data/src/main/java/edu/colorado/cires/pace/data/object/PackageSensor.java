package edu.colorado.cires.pace.data.object;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode
@Builder(toBuilder = true)
@Jacksonized
public class PackageSensor {
  @NotBlank
  private final String name;
  @NotNull @Valid
  private final Position position;
}

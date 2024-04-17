package edu.colorado.cires.pace.data.object;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class MobileMarineLocation implements MarineLocation {
  
  private final Sea seaArea;
  @NotNull @Valid
  private final Ship vessel;
  @NotBlank
  private final String locationDerivationDescription;
  
}

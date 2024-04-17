package edu.colorado.cires.pace.data.object;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class Position {
  
  @NotNull
  private final Float x;
  @NotNull
  private final Float y;
  @NotNull
  private final Float z;
  
}

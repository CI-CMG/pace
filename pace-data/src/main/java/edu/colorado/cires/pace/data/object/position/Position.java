package edu.colorado.cires.pace.data.object.position;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.jackson.Jacksonized;

/**
 * Position holds an x, y, and z value
 */
@Data
@EqualsAndHashCode
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

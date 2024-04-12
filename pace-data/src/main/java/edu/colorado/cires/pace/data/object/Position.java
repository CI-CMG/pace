package edu.colorado.cires.pace.data.object;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
public class Position {
  private final Float x;
  private final Float y;
  private final Float z;

  @Builder(toBuilder = true)
  @Jacksonized
  public Position(Float x, Float y, Float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
}

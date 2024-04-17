package edu.colorado.cires.pace.data.object;

import jakarta.validation.constraints.NotBlank;

public interface ObjectWithName extends ObjectWithUniqueField {
  @NotBlank
  String getName();

}

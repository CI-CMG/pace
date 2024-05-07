package edu.colorado.cires.pace.data.object;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public interface TabularTranslationField {
  
  @NotBlank
  String getPropertyName();
  @Positive
  Integer getColumnNumber();

}

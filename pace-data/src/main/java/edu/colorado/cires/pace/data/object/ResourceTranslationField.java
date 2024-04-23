package edu.colorado.cires.pace.data.object;

import jakarta.validation.constraints.NotBlank;

public interface ResourceTranslationField extends TabularTranslationField {
  @NotBlank
  String getResourceName();
}

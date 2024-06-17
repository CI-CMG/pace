package edu.colorado.cires.pace.data.translator;

import jakarta.validation.constraints.NotNull;

public interface TimeTranslator {
  @NotNull
  String getTimeZone();

}

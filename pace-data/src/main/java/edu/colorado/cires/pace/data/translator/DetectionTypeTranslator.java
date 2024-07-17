package edu.colorado.cires.pace.data.translator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class DetectionTypeTranslator extends Translator {
  private final String detectionTypeUUID;
  private final String source;
  private final String scienceName;
}

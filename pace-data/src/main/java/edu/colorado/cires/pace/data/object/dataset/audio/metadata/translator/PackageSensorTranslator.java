package edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode
@Builder(toBuilder = true)
@Jacksonized
public class PackageSensorTranslator {
  
  private final String name;
  private final PositionTranslator position;

}

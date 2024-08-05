package edu.colorado.cires.passivePacker.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class PassivePackerAudioSensor extends PassivePackerSensor {
  
  private final String hydroId;
  private final String preId;

}

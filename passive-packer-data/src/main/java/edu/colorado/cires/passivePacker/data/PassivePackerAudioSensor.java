package edu.colorado.cires.passivePacker.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * PassivePackerAudioSensor extends PassivePackerSensor and provides builder structure for
 * PassivePackerAudioSensor objects
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class PassivePackerAudioSensor extends PassivePackerSensor {
  
  private final String hydroId;
  private final String preId;

}

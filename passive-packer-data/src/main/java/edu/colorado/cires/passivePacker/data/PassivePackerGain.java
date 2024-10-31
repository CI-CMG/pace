package edu.colorado.cires.passivePacker.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * PassivePackerGain extends PassivePackerTimeRange provides builder structure for
 * PassivePackerGain objects
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class PassivePackerGain extends PassivePackerTimeRange {
  
  @JsonProperty("GAIN_dB")
  private final String gain;

}

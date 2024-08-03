package edu.colorado.cires.pace.data.object.dataset.passivePacker;

import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class PassivePackerAudioPackage extends PassivePackerPackage {
  
  private final Map<Integer, PassivePackerChannel> channels;
  private final PassivePackerQualityDetails qualityDetails;
  private final String instrumentId;

}

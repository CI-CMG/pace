package edu.colorado.cires.pace.data.translator;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@Jacksonized
public class CPODPackageTranslator extends PackageTranslator {

  private final String instrumentId;
  private final String hydrophoneSensitivity;
  private final String frequencyRange;
  private final String gain;

  private final QualityControlDetailTranslator qualityControlDetailTranslator;

  private final String deploymentTime;
  private final String recoveryTime;
  private final String comments;
  private final String sensors;
  private final List<ChannelTranslator> channelTranslators;

}

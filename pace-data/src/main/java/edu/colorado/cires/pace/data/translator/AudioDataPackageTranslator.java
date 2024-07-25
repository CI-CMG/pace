package edu.colorado.cires.pace.data.translator;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
public abstract class AudioDataPackageTranslator extends PackageTranslator {

  private final String instrumentId;
  private final String hydrophoneSensitivity;
  private final String frequencyRange;
  private final String gain;

  private final QualityControlDetailTranslator qualityControlDetailTranslator;

  private final TimeTranslator deploymentTime;
  private final TimeTranslator recoveryTime;
  private final String comments;
  private final List<PackageSensorTranslator> sensors;
  @Builder.Default
  private final List<ChannelTranslator> channelTranslators = new ArrayList<>(0);
  
  

}

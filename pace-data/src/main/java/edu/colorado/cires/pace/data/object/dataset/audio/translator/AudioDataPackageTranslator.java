package edu.colorado.cires.pace.data.object.dataset.audio.translator;

import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.ChannelTranslator;
import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.PackageSensorTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.translator.PackageTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.QualityControlDetailTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * AudioDataPackageTranslator holds all the headers for fields of a package translator
 * in addition to fields that only apply to audio data packages
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
public abstract class AudioDataPackageTranslator extends PackageTranslator {

  private final String instrumentId;

  private final QualityControlDetailTranslator qualityControlDetailTranslator;

  private final TimeTranslator deploymentTime;
  private final TimeTranslator recoveryTime;
  private final TimeTranslator audioStartTime;
  private final TimeTranslator audioEndTime;
  private final String comments;
  @Builder.Default
  private final List<PackageSensorTranslator> sensors = Collections.emptyList();
  @Builder.Default
  private final List<ChannelTranslator> channelTranslators = Collections.emptyList();
  
  

}

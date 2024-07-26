package edu.colorado.cires.pace.data.object.dataset.base.metadata.translator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
public abstract class SoundAnalysisPackageTranslator extends SoftwareDependentPackageTranslator {

  private final String analysisTimeZone;
  private final String analysisEffort;
  private final String sampleRate;
  private final String minFrequency;
  private final String maxFrequency;

}

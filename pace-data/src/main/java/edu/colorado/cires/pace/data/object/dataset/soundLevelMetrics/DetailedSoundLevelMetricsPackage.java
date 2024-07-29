package edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.dataset.base.DetailedPackage;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntry;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class DetailedSoundLevelMetricsPackage extends DetailedPackage implements BaseSoundLevelMetricsPackage<AbstractObject> {
  private final LocalDateTime audioStartTime;
  private final LocalDateTime audioEndTime;
  private final AbstractObject qualityAnalyst;
  private final String qualityAnalysisObjectives;
  private final String qualityAnalysisMethod;
  private final String qualityAssessmentDescription;
  private final List<DataQualityEntry> qualityEntries;
  private final Integer analysisTimeZone;
  private final Integer analysisEffort;
  private final Float sampleRate;
  private final Float minFrequency;
  private final Float maxFrequency;
  private final String softwareNames;
  private final String softwareVersions;
  private final String softwareProtocolCitation;
  private final String softwareDescription;
  private final String softwareProcessingDescription;

  @Override
  public DetailedSoundLevelMetricsPackage setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public DetailedSoundLevelMetricsPackage setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }

  @Override
  public DetailedSoundLevelMetricsPackage setQualityAnalyst(AbstractObject qualityAnalyst) {
    return toBuilder().qualityAnalyst(qualityAnalyst).build();
  }
}

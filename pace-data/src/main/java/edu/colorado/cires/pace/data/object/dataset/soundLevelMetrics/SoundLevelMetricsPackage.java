package edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics;

import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntry;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * SoundLevelMetricsPackage extends Package and holds onto the relevant data of a
 * SoundLevelMetrics package
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class SoundLevelMetricsPackage extends Package implements BaseSoundLevelMetricsPackage<String> {
  private final LocalDateTime audioStartTime;
  private final LocalDateTime audioEndTime;
  private final String qualityAnalyst;
  private final String qualityAnalysisObjectives;
  private final String qualityAnalysisMethod;
  private final String qualityAssessmentDescription;
  @Builder.Default
  private final List<DataQualityEntry> qualityEntries = Collections.emptyList();
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
  private final LocalDateTime startTime;
  private final LocalDateTime endTime;

  /**
   * Returns a new package with the quality analyst set to the provided value
   *
   * @param qualityAnalyst to set in new object
   * @return SoundLevelMetricsPackage with quality analyst set to provided value
   */
  @Override
  public SoundLevelMetricsPackage setQualityAnalyst(String qualityAnalyst) {
    return toBuilder().qualityAnalyst(qualityAnalyst).build();
  }

  /**
   * Returns a new package with the uuid set to the provided value
   *
   * @param uuid field for assigning uuid to new object
   * @return SoundLevelMetricsPackage with uuid set to provided value
   */
  @Override
  public SoundLevelMetricsPackage setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  /**
   * Returns a new package with the visibility set to the provided value
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return SoundLevelMetricsPackage with visibility set to provided value
   */
  @Override
  public SoundLevelMetricsPackage setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}

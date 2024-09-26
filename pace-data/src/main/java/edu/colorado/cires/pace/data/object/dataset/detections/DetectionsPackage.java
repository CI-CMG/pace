package edu.colorado.cires.pace.data.object.dataset.detections;

import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.TimeRange;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntry;
import jakarta.validation.constraints.NotBlank;
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
 * DetectionsPackage holds all the values which are necessarily part of
 * a detections package
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class DetectionsPackage extends Package implements BaseDetectionsPackage<String>, TimeRange {
  @NotBlank
  private final String soundSource;
  
  private final String qualityAnalyst;
  private final String qualityAnalysisObjectives;
  private final String qualityAnalysisMethod;
  private final String qualityAssessmentDescription;
  @Builder.Default
  private final List<DataQualityEntry> qualityEntries = Collections.emptyList();
  private final String softwareNames;
  private final String softwareVersions;
  private final String softwareProtocolCitation;
  private final String softwareDescription;
  private final String softwareProcessingDescription;
  private final Integer analysisTimeZone;
  private final Integer analysisEffort;
  private final Float sampleRate;
  private final Float minFrequency;
  private final Float maxFrequency;
  private final LocalDateTime startTime;
  private final LocalDateTime endTime;

  /**
   * Returns a new package with the quality analyst set to the
   * provided value
   *
   * @param qualityAnalyst to set in new object
   * @return DetectionsPackage with provided qualityAnalyst set
   */
  @Override
  public DetectionsPackage setQualityAnalyst(String qualityAnalyst) {
    return toBuilder().qualityAnalyst(qualityAnalyst).build();
  }

  /**
   * Returns a new package with the uuid set to the provided value
   *
   * @param uuid field for assigning uuid to new object
   * @return DetectionsPackage with uuid set
   */
  @Override
  public DetectionsPackage setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  /**
   * Returns a new package with the visibility set to the provided value
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return DetectionsPackage with visibility set
   */
  @Override
  public DetectionsPackage setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }

  /**
   * Returns a new package with the soundSource set to the provided value
   *
   * @param soundSource to set in returned package
   * @return DetectionsPackage with soundSource set
   */
  @Override
  public DetectionsPackage setSoundSource(String soundSource) {
    return toBuilder().soundSource(soundSource).build();
  }
}

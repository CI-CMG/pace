package edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics;

import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntry;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.LocationDetail;
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
public class SoundLevelMetricsPackage extends Package implements BaseSoundLevelMetricsPackage<String> {
  private final LocalDateTime audioStartTime;
  private final LocalDateTime audioEndTime;
  private final String qualityAnalyst;
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
  public SoundLevelMetricsPackage setLocationDetail(LocationDetail locationDetail) {
    return toBuilder().locationDetail(locationDetail).build();
  }

  @Override
  public SoundLevelMetricsPackage setProjectName(List<String> projectName) {
    return toBuilder().projectName(projectName).build();
  }

  @Override
  public SoundLevelMetricsPackage setPlatformName(String platformName) {
    return toBuilder().platformName(platformName).build();
  }

  @Override
  public SoundLevelMetricsPackage setQualityAnalyst(String qualityAnalyst) {
    return toBuilder().qualityAnalyst(qualityAnalyst).build();
  }

  @Override
  public SoundLevelMetricsPackage setScientists(List<String> scientists) {
    return toBuilder().scientists(scientists).build();
  }

  @Override
  public SoundLevelMetricsPackage setDatasetPackager(String datasetPackager) {
    return toBuilder().datasetPackager(datasetPackager).build();
  }

  @Override
  public SoundLevelMetricsPackage setSponsors(List<String> sponsors) {
    return toBuilder().sponsors(sponsors).build();
  }

  @Override
  public SoundLevelMetricsPackage setFunders(List<String> funders) {
    return toBuilder().funders(funders).build();
  }

  @Override
  public SoundLevelMetricsPackage setInstrumentType(String instrumentType) {
    return toBuilder().instrumentType(instrumentType).build();
  }

  @Override
  public SoundLevelMetricsPackage setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public SoundLevelMetricsPackage setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}

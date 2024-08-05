package edu.colorado.cires.pace.data.object.dataset.detections;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntry;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.LocationDetail;
import jakarta.validation.constraints.NotBlank;
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
public class DetectionsPackage extends Package implements BaseDetectionsPackage<String> {
  @NotBlank
  private final String soundSource;
  
  private final String qualityAnalyst;
  private final String qualityAnalysisObjectives;
  private final String qualityAnalysisMethod;
  private final String qualityAssessmentDescription;
  private final List<DataQualityEntry> qualityEntries;
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

  @Override
  public DetectionsPackage setLocationDetail(LocationDetail locationDetail) {
    return toBuilder().locationDetail(locationDetail).build();
  }

  @Override
  public DetectionsPackage setProjects(List<String> projects) {
    return toBuilder().projects(projects).build();
  }

  @Override
  public DetectionsPackage setPlatform(String platform) {
    return toBuilder().platform(platform).build();
  }

  @Override
  public DetectionsPackage setQualityAnalyst(String qualityAnalyst) {
    return toBuilder().qualityAnalyst(qualityAnalyst).build();
  }

  @Override
  public DetectionsPackage setScientists(List<String> scientists) {
    return toBuilder().scientists(scientists).build();
  }

  @Override
  public DetectionsPackage setDatasetPackager(String datasetPackager) {
    return toBuilder().datasetPackager(datasetPackager).build();
  }

  @Override
  public DetectionsPackage setSponsors(List<String> sponsors) {
    return toBuilder().sponsors(sponsors).build();
  }

  @Override
  public DetectionsPackage setFunders(List<String> funders) {
    return toBuilder().funders(funders).build();
  }

  @Override
  public DetectionsPackage setInstrument(String instrument) {
    return toBuilder().instrument(instrument).build();
  }

  @Override
  public DetectionsPackage setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public DetectionsPackage setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }

  @Override
  public DetectionsPackage setSoundSource(String soundSource) {
    return toBuilder().soundSource(soundSource).build();
  }
}

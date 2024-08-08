package edu.colorado.cires.pace.data.object.dataset.base;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.CPODPackage;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.LocationDetail;
import edu.colorado.cires.pace.data.object.dataset.detections.DetectionsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundClips.SoundClipsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.SoundLevelMetricsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundPropagationModels.SoundPropagationModelsPackage;
import edu.colorado.cires.pace.data.validation.ValidPackageIdentifiers;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@JsonTypeInfo(use = Id.NAME, include = As.EXTERNAL_PROPERTY, property = "type")
@JsonSubTypes({
    @Type(value = AudioPackage.class, name = "audio"),
    @Type(value = CPODPackage.class, name = "CPOD"),
    @Type(value = DetectionsPackage.class, name = "detections"),
    @Type(value = SoundClipsPackage.class, name = "sound clips"),
    @Type(value = SoundLevelMetricsPackage.class, name = "sound level metrics"),
    @Type(value = SoundPropagationModelsPackage.class, name = "sound propagation models"),
})
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@ValidPackageIdentifiers
public class Package extends BasePackage<String> {
  private final UUID uuid;
  @Default
  private final boolean visible = true;
  @NotBlank
  private final String datasetPackager;
  @NotEmpty @NotNull @Builder.Default
  private final List<@NotBlank String> scientists = Collections.emptyList();
  @NotNull @Builder.Default
  private final List<@NotBlank String> projects = Collections.emptyList();
  @NotNull @NotEmpty @Builder.Default
  private final List<@NotBlank String> sponsors = Collections.emptyList();
  @NotNull @NotEmpty @Builder.Default
  private final List<@NotBlank String> funders = Collections.emptyList();
  @NotBlank
  private final String platform;
  @NotBlank
  private final String instrument;

  @Override
  protected List<String> getProjectNames() {
    return this.getProjects() == null ? Collections.emptyList() : this.getProjects();
  }
  
  public Package setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }
  
  public Package setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }

  public Package setLocationDetail(LocationDetail locationDetail) {
    return toBuilder().locationDetail(locationDetail).build();
  }
  public Package setProjects(List<String> projects) {
    return toBuilder().projects(projects).build();
  }
  public Package setPlatform(String platform) {
    return toBuilder().platform(platform).build();
  }
  public Package setScientists(List<String> scientists) {
    return toBuilder().scientists(scientists).build();
  }
  public Package setDatasetPackager(String datasetPackager) {
    return toBuilder().datasetPackager(datasetPackager).build();
  }
  public Package setSponsors(List<String> sponsors) {
    return toBuilder().sponsors(sponsors).build();
  }
  public Package setFunders(List<String> funders) {
    return toBuilder().funders(funders).build();
  }
  public Package setInstrument(String instrument) {
    return toBuilder().instrument(instrument).build();
  }
  
  public <P extends Package, B extends Package.PackageBuilder<P, ?>> P toInheritingType(B inheritingTypeBuilder) {
    return inheritingTypeBuilder
        .uuid(getUuid())
        .visible(isVisible())
        .processingLevel(getProcessingLevel())
        .temperaturePath(getTemperaturePath())
        .biologicalPath(getBiologicalPath())
        .otherPath(getOtherPath())
        .documentsPath(getDocumentsPath())
        .calibrationDocumentsPath(getCalibrationDocumentsPath())
        .sourcePath(getSourcePath())
        .dataCollectionName(getDataCollectionName())
        .siteOrCruiseName(getSiteOrCruiseName())
        .deploymentId(getDeploymentId())
        .projects(getProjects())
        .publicReleaseDate(getPublicReleaseDate())
        .locationDetail(getLocationDetail())
        .deploymentTitle(getDeploymentTitle())
        .deploymentPurpose(getDeploymentPurpose())
        .deploymentDescription(getDeploymentDescription())
        .alternateSiteName(getAlternateSiteName())
        .alternateDeploymentName(getAlternateDeploymentName())
        .startTime(getStartTime())
        .endTime(getEndTime())
        .preDeploymentCalibrationDate(getPreDeploymentCalibrationDate())
        .postDeploymentCalibrationDate(getPostDeploymentCalibrationDate())
        .calibrationDescription(getCalibrationDescription())
        .datasetPackager(getDatasetPackager())
        .scientists(getScientists())
        .sponsors(getSponsors())
        .funders(getFunders())
        .platform(getPlatform())
        .instrument(getInstrument())
        .build();
  }
}

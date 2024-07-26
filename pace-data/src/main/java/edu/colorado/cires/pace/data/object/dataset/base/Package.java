package edu.colorado.cires.pace.data.object.dataset.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;
import edu.colorado.cires.pace.data.object.dataset.audio.CPODPackage;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.CalibrationDetail;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.TimeRange;
import edu.colorado.cires.pace.data.object.dataset.detections.DetectionsPackage;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.LocationDetail;
import edu.colorado.cires.pace.data.object.dataset.soundClips.SoundClipsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.SoundLevelMetricsPackage;
import edu.colorado.cires.pace.data.object.dataset.soundPropagationModels.SoundPropagationModelsPackage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@JsonTypeInfo(use = Id.NAME, include = As.EXTERNAL_PROPERTY, property = "datasetType")
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
public abstract class Package extends ObjectWithUniqueField implements TimeRange, CalibrationDetail, DatasetDependencies<String> {

  private final UUID uuid;
  private final Path temperaturePath;
  private final Path biologicalPath;
  private final Path otherPath;
  private final Path documentsPath;
  private final Path calibrationDocumentsPath;
  private final Path navigationPath;
  @NotNull
  private final Path sourcePath;

  @NotBlank
  private final String siteOrCruiseName;
  @NotBlank
  private final String deploymentId;
  @NotNull
  @NotEmpty
  private final List<@NotBlank String> projects;
  @NotNull
  private final LocalDate publicReleaseDate;
  @NotNull @Valid
  private final LocationDetail locationDetail;
  @NotNull @NotEmpty
  private final List<@NotBlank String> sponsors;
  @NotNull @NotEmpty
  private final List<@NotBlank String> funders;
  private final String deploymentTitle;
  private final String deploymentPurpose;
  private final String deploymentDescription;
  private final String alternateSiteName;
  private final String alternateDeploymentName;
  @NotBlank
  private final String platform;
  @NotBlank
  private final String instrument;
  @NotNull
  private final LocalDateTime startTime;
  @NotNull
  private final LocalDateTime endTime;
  @NotNull
  private final LocalDate preDeploymentCalibrationDate;
  @NotNull
  private final LocalDate postDeploymentCalibrationDate;
  private final String calibrationDescription;

  @NotBlank
  private final String datasetPackager;
  @NotNull
  @NotEmpty
  private final List<@NotBlank String> scientists;

  @Override
  public String getUniqueField() {
    return getPackageId();
  }

  @JsonIgnore
  public String getPackageId() {
    String packageId = null;

    List<String> projects = getProjects() == null ? Collections.emptyList() : getProjects();
    if (!projects.isEmpty()) {
      packageId = projects.get(0);
    }

    String siteCruiseName = getSiteOrCruiseName();
    if (siteCruiseName != null) {
      if (packageId == null) {
        packageId = siteCruiseName;
      } else {
        packageId += String.format(
            "_%s", siteCruiseName
        );
      }
    }

    String deploymentId = getDeploymentId();
    if (deploymentId != null) {
      if (packageId == null) {
        packageId = deploymentId;
      } else {
        packageId += String.format(
            "_%s", deploymentId
        );
      }
    }

    return packageId;
  }

  public abstract Package setLocationDetail(LocationDetail locationDetail);
  public abstract Package setProjects(List<String> projects);
  public abstract Package setPlatform(String platform);
  public abstract Package setScientists(List<String> scientists);
  public abstract Package setDatasetPackager(String datasetPackager);
  public abstract Package setSponsors(List<String> sponsors);
  public abstract Package setFunders(List<String> funders);
  public abstract Package setInstrument(String instrument);

}

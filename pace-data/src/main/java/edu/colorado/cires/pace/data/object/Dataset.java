package edu.colorado.cires.pace.data.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public abstract class Dataset extends ObjectWithUniqueField implements TimeRange, CalibrationDetail {
  @NotBlank
  private final String siteOrCruiseName;
  @NotBlank
  private final String deploymentId;
  @NotBlank
  private final String datasetPackager;
  @NotNull @NotEmpty
  private final List<@NotBlank String> projects;
  @NotNull
  private final LocalDate publicReleaseDate;
  @NotNull @Valid
  private final LocationDetail locationDetail;
  @NotNull @NotEmpty
  private final List<@NotBlank String> scientists;
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
}

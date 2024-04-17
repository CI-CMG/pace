package edu.colorado.cires.pace.data.object;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public interface PackageInfo {
  @NotBlank
  String getSiteOrCruiseName();
  @NotBlank
  String getDeploymentId();
  @NotNull @Valid
  Person getDatasetPackager();
  @NotNull @NotEmpty
  List<@Valid Project> getProjects();
  @NotNull
  LocalDate getPublicReleaseDate();
  
  default String getPackageId() {
    String packageId = null;
    
    List<Project> projects = getProjects();
    if (!projects.isEmpty()) {
      Project project = projects.get(0);
      packageId = project.getName();
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

package edu.colorado.cires.pace.data.object.dataset.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.CalibrationDetail;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.TimeRange;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.LocationDetail;
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
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

/**
 * BasePackage provides the relevant fields for packages as well as getters
 * for unique field and package ID
 */
@Data
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
public abstract class BasePackage implements AbstractObject, CalibrationDetail {

  private final UUID uuid;
  @NotNull
  @Builder.Default
  private final boolean visible = true;
  private final Path temperaturePath;
  private final Path biologicalPath;
  private final Path otherPath;
  private final Path documentsPath;
  private final Path calibrationDocumentsPath;
  @NotNull
  private final Path sourcePath;

  private final String dataCollectionName;
  private final String siteOrCruiseName;
  private final String deploymentId;
  
  @NotNull
  private final LocalDate publicReleaseDate;
  @NotNull @Valid
  private final LocationDetail locationDetail;
  private final String deploymentTitle;
  private final String deploymentPurpose;
  private final String deploymentDescription;
  private final String alternateSiteName;
  private final String alternateDeploymentName;
  private final LocalDate preDeploymentCalibrationDate;
  private final LocalDate postDeploymentCalibrationDate;
  private final String calibrationDescription;

  private final String datasetPackager;
  @Builder.Default
  private final List<String> scientists = Collections.emptyList();
  @Builder.Default @NotNull @NotEmpty
  private final List<@NotBlank String> sponsors = Collections.emptyList();
  @Builder.Default
  private final List<String> funders = Collections.emptyList();
  @NotBlank
  private final String platform;
  @NotBlank
  private final String instrument;
  @Builder.Default
  private final List<@NotBlank String> projects = Collections.emptyList();

  /**
   * Returns unique field
   * @return String unique field
   */
  @Override
  public String getUniqueField() {
    return getPackageId();
  }

  /**
   * Returns the package ID, prioritizing first the dataCollectionName if
   * it exists, then generating a package ID otherwise based on project name,
   * site/cruise name, and deploymentID
   * @return String package ID
   */
  @JsonIgnore
  public String getPackageId() {
    if (StringUtils.isNotBlank(dataCollectionName)) {
      return dataCollectionName;
    }
    
    String packageId = null;

    List<String> projects = getProjects();
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

  /**
   * Creates a BasePackage using a builder
   */
  public abstract static class BasePackageBuilder<C extends BasePackage, B> {
    public B baseFields(BasePackage p) {
      $fillValuesFromInstanceIntoBuilder(p, this);
      return self();
    }
  }

}

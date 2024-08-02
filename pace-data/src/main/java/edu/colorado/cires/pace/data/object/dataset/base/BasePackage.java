package edu.colorado.cires.pace.data.object.dataset.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.CalibrationDetail;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.TimeRange;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.LocationDetail;
import jakarta.validation.Valid;
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

@Data
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
public abstract class BasePackage<T> implements AbstractObject, TimeRange, CalibrationDetail {

  private final UUID uuid;
  @NotNull
  @Builder.Default
  private final boolean visible = true;
  private final Path temperaturePath;
  private final Path biologicalPath;
  private final Path otherPath;
  private final Path documentsPath;
  private final Path calibrationDocumentsPath;
  private final Path navigationPath;
  @NotNull
  private final Path sourcePath;

  private final String dataCollectionName;
  private final String site;
  private final String deploymentId;
  @NotNull
  protected abstract List<@NotNull @Valid T> getProjectName();
  
  @NotNull
  private final LocalDate publishDate;
  @NotNull @Valid
  private final LocationDetail locationDetail;
  private final String title;
  private final String purpose;
  private final String deploymentDescription;
  private final String alternateSiteName;
  private final String deploymentAlias;
  @NotNull
  private final LocalDateTime startTime;
  @NotNull
  private final LocalDateTime endTime;
  @NotNull
  private final LocalDate preDeploymentCalibrationDate;
  @NotNull
  private final LocalDate postDeploymentCalibrationDate;
  private final String calibrationDescription;

  @NotNull
  @Valid
  protected abstract T getDatasetPackager();
  @NotNull
  @NotEmpty
  protected abstract List<@NotNull @Valid T> getScientists();
  @NotNull @NotEmpty
  protected abstract List<@NotNull @Valid T> getSponsors();
  @NotNull @NotEmpty
  protected abstract List<@NotNull @Valid T> getFunders();
  @NotNull @Valid
  protected abstract T getPlatformName();
  @NotNull @Valid
  protected abstract T getInstrumentType();
  
  @JsonIgnore
  protected abstract List<String> getProjectNames();

  @Override
  public String getUniqueField() {
    return getPackageId();
  }

  @JsonIgnore
  public String getPackageId() {
    if (StringUtils.isNotBlank(dataCollectionName)) {
      return dataCollectionName;
    }
    
    String packageId = null;

    List<String> projects = getProjectName() == null ? Collections.emptyList() : getProjectNames();
    if (!projects.isEmpty()) {
      packageId = projects.get(0);
    }

    String siteCruiseName = getSite();
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

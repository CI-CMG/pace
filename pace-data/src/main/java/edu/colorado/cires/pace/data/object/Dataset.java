package edu.colorado.cires.pace.data.object;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface Dataset extends PackageInfo, PeopleOrganizationsInfo, DatasetDetail, LocationDetail, CalibrationDetail, DatasetDescription {
  @NotNull @Valid
  LocationDetail getLocationDetail();
}

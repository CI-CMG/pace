package edu.colorado.cires.pace.data.object;

public interface Dataset<DD extends DatasetDetail, LD extends LocationDetail, CD extends CalibrationDetail, DDesc extends DatasetDescription> {
  
  PackageInfo getPackageInfo();
  PeopleOrganizationsInfo getPeopleOrganizationInfo();
  
  DDesc getDatasetDescription();
  DD getDatasetDetail();
  LD getLocationDetail();
  CD getCalibrationDetail();
  
}

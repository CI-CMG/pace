package edu.colorado.cires.pace.data.object;

import java.time.LocalDate;

public interface CalibrationDetail {
  
  LocalDate getPreDeploymentCalibrationDate();
  LocalDate getPostDeploymentCalibrationDate();
  String getCalibrationDescription();

}

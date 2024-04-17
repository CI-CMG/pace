package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.validation.ValidCalibrationDetail;
import java.time.LocalDate;

@ValidCalibrationDetail
public interface CalibrationDetail {
  LocalDate getPreDeploymentCalibrationDate();
  LocalDate getPostDeploymentCalibrationDate();
  String getCalibrationDescription();

}

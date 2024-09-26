package edu.colorado.cires.pace.data.object.dataset.base.metadata;

import edu.colorado.cires.pace.data.validation.ValidCalibrationDetail;
import java.time.LocalDate;

/**
 * CalibrationDetail provides getters for calibration dates and description
 */
@ValidCalibrationDetail
public interface CalibrationDetail {

  /**
   * Returns pre-deployment calibration date
   * @return LocalDate pre-deployment calibration date
   */
  LocalDate getPreDeploymentCalibrationDate();

  /**
   * Returns post-deployment calibration date
   * @return LocalDate post-deployment calibration date
   */
  LocalDate getPostDeploymentCalibrationDate();

  /**
   * Returns calibration description
   * @return String calibration description
   */
  String getCalibrationDescription();

}

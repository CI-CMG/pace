package edu.colorado.cires.pace.data.object;

import java.nio.file.Path;

public interface CalibrationDetailWithPath extends CalibrationDetail {

  Path getCalibrationDocumentsPath();

}

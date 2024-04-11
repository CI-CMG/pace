package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.PackingJob;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class PackingJobValidator extends BaseValidator<PackingJob> {

  @Override
  protected Set<ConstraintViolation> runValidation(PackingJob object) {
    Set<ConstraintViolation> violations = new HashSet<>();
    
    Path sourcePath = object.getSourcePath();
    if (sourcePath == null) {
      violations.add(new ConstraintViolation(
          "sourcePath", "sourcePath must be defined"
      ));
    }

    checkPath("sourcePath", sourcePath, violations);
    checkPath("biologicalPath", object.getBiologicalPath(), violations);
    checkPath("calibrationDocumentsPath", object.getCalibrationDocumentsPath(), violations);
    checkPath("documentsPath", object.getDocumentsPath(), violations);
    checkPath("navigationPath", object.getNavigationPath(), violations);
    checkPath("otherPath", object.getOtherPath(), violations);
    checkPath("temperaturePath", object.getTemperaturePath(), violations);
    
    return violations;
  }
  
  private static void checkPath(String propertyName, Path path, Set<ConstraintViolation> violations) {
    if (path == null) {
      return;
    }

    if (StringUtils.isBlank(path.toString())) {
      violations.add(new ConstraintViolation(
          propertyName, String.format(
              "%s must not be blank", propertyName
          )
      ));
      return;
    }
    
    if (!path.isAbsolute()) {
      violations.add(new ConstraintViolation(
          propertyName, String.format(
              "%s must be an absolute path", propertyName
          )
      ));
    }
  }
}
package edu.colorado.cires.pace.core.validation;

import edu.colorado.cires.pace.data.PackingJob;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class PackingJobValidator implements Validator<PackingJob> {

  @Override
  public Set<ConstraintViolation> validate(PackingJob object) {
    Set<ConstraintViolation> violations = new HashSet<>();
    
    String sourcePath = object.getSourcePath();
    if (StringUtils.isBlank(sourcePath)) {
      violations.add(new ConstraintViolation(
          "sourcePath", "sourcePath must not be blank"
      ));
    }

    checkIsAbsolutePath("sourcePath", sourcePath, violations);
    checkIsAbsolutePath("biologicalPath", object.getBiologicalPath(), violations);
    checkIsAbsolutePath("calibrationDocumentsPath", object.getCalibrationDocumentsPath(), violations);
    checkIsAbsolutePath("documentsPath", object.getDocumentsPath(), violations);
    checkIsAbsolutePath("navigationPath", object.getNavigationPath(), violations);
    checkIsAbsolutePath("otherPath", object.getOtherPath(), violations);
    checkIsAbsolutePath("temperaturePath", object.getTemperaturePath(), violations);
    
    return violations;
  }
  
  private static void checkIsAbsolutePath(String propertyName, String argument, Set<ConstraintViolation> violations) {
    if (StringUtils.isBlank(argument)) {
      return;
    }
    
    Path path = Path.of(argument);
    
    if (!path.isAbsolute()) {
      violations.add(new ConstraintViolation(
          propertyName, String.format(
              "%s must be an absolute path", propertyName
          )
      ));
    }
  }
}

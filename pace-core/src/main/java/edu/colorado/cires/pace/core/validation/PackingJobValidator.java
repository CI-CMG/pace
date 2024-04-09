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
    
    Path sourcePath = object.sourcePath();
    if (sourcePath == null) {
      violations.add(new ConstraintViolation(
          "sourcePath", "sourcePath must be defined"
      ));
    }

    checkPath("sourcePath", sourcePath, violations);
    checkPath("biologicalPath", object.biologicalPath(), violations);
    checkPath("calibrationDocumentsPath", object.calibrationDocumentsPath(), violations);
    checkPath("documentsPath", object.documentsPath(), violations);
    checkPath("navigationPath", object.navigationPath(), violations);
    checkPath("otherPath", object.otherPath(), violations);
    checkPath("temperaturePath", object.temperaturePath(), violations);
    
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

package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.FileType;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class FileTypeValidator extends BaseValidator<FileType> {

  @Override
  public Set<ConstraintViolation> runValidation(FileType object) {
    Set<ConstraintViolation> violations = new HashSet<>(0);

    if (StringUtils.isBlank(object.getType())) {
      violations.add(new ConstraintViolation(
          "type", "type must not be blank"
      ));
    }

    return violations;
  }
}

package edu.colorado.cires.pace.core.validation;

import edu.colorado.cires.pace.data.FileType;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class FileTypeValidator implements Validator<FileType> {

  @Override
  public Set<ConstraintViolation> validate(FileType object) {
    Set<ConstraintViolation> violations = new HashSet<>(0);

    if (StringUtils.isBlank(object.getType())) {
      violations.add(new ConstraintViolation(
          "type", "type must not be blank"
      ));
    }

    return violations;
  }
}

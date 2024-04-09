package edu.colorado.cires.pace.core.validation;

import edu.colorado.cires.pace.data.CSVTranslator;
import edu.colorado.cires.pace.data.CSVTranslatorField;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class CSVTranslatorValidator implements Validator<CSVTranslator> {

  @Override
  public Set<ConstraintViolation> validate(CSVTranslator object) {
    Set<ConstraintViolation> violations = new HashSet<>(0);
    
    if (StringUtils.isBlank(object.name())) {
      violations.add(new ConstraintViolation(
          "name", "name must not be blank"
      ));
    }

    if (object.fields() == null || object.fields().isEmpty()) {
      violations.add(new ConstraintViolation(
          "fields", "fields must not be empty"
      ));
    } else {
      for (int i = 0; i < object.fields().size(); i++) {
        CSVTranslatorField field = object.fields().get(i);

        if (StringUtils.isBlank(field.propertyName())) {
          violations.add(new ConstraintViolation(
              String.format("fields[%s].propertyName", i), "propertyName must not be blank"
          ));
        }

        long column = field.columnNumber();
        if (column <= 0) {
          violations.add(new ConstraintViolation(
              String.format("fields[%s].columnNumber", i), "columnNumber must be greater than 0"
          ));
        }
      }

      if (object.fields().size() != object.fields().stream().map(CSVTranslatorField::propertyName).distinct().count()) {
        violations.add(new ConstraintViolation(
            "fields", "fields contain duplicate property names"
        ));
      }

      if (object.fields().size() != object.fields().stream().map(CSVTranslatorField::columnNumber).distinct().count()) {
        violations.add(new ConstraintViolation(
            "fields", "fields contain duplicate column numbers"
        ));
      }
    }
    
    return violations;
  }
}

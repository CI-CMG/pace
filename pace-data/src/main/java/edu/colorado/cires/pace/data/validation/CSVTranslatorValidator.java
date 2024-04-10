package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.data.object.CSVTranslatorField;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class CSVTranslatorValidator extends BaseValidator<CSVTranslator> {

  @Override
  public Set<ConstraintViolation> runValidation(CSVTranslator object) {
    Set<ConstraintViolation> violations = new HashSet<>(0);
    
    if (StringUtils.isBlank(object.getName())) {
      violations.add(new ConstraintViolation(
          "name", "name must not be blank"
      ));
    }

    if (object.getFields() == null || object.getFields().isEmpty()) {
      violations.add(new ConstraintViolation(
          "fields", "fields must not be empty"
      ));
    } else {
      for (int i = 0; i < object.getFields().size(); i++) {
        CSVTranslatorField field = object.getFields().get(i);

        if (StringUtils.isBlank(field.getPropertyName())) {
          violations.add(new ConstraintViolation(
              String.format("fields[%s].propertyName", i), "propertyName must not be blank"
          ));
        }

        long column = field.getColumnNumber();
        if (column <= 0) {
          violations.add(new ConstraintViolation(
              String.format("fields[%s].columnNumber", i), "columnNumber must be greater than 0"
          ));
        }
      }

      if (object.getFields().size() != object.getFields().stream().map(CSVTranslatorField::getPropertyName).distinct().count()) {
        violations.add(new ConstraintViolation(
            "fields", "fields contain duplicate property names"
        ));
      }

      if (object.getFields().size() != object.getFields().stream().map(CSVTranslatorField::getColumnNumber).distinct().count()) {
        violations.add(new ConstraintViolation(
            "fields", "fields contain duplicate column numbers"
        ));
      }
    }
    
    return violations;
  }
}

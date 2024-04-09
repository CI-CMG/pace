package edu.colorado.cires.pace.core.validation;

import edu.colorado.cires.pace.data.ExcelTranslator;
import edu.colorado.cires.pace.data.ExcelTranslatorField;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class ExcelTranslatorValidator implements Validator<ExcelTranslator> {

  @Override
  public Set<ConstraintViolation> validate(ExcelTranslator object) {
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
        ExcelTranslatorField field = object.fields().get(i);

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
        
        long sheet = field.sheetNumber();
        if (sheet <= 0) {
          violations.add(new ConstraintViolation(
              String.format("fields[%s].sheetNumber", i), "sheetNumber must be greater than 0"
          ));
        }
      }

      if (object.fields().size() != object.fields().stream().map(ExcelTranslatorField::propertyName).distinct().count()) {
        violations.add(new ConstraintViolation(
            "fields", "fields contain duplicate property names"
        ));
      }

      if (object.fields().size() != object.fields().stream().map(f -> String.format("%s_%s", f.columnNumber(), f.sheetNumber())).distinct().count()) {
        violations.add(new ConstraintViolation(
            "fields", "fields contain duplicate column/sheet number pairs"
        ));
      }
    }

    return violations;
  }
}

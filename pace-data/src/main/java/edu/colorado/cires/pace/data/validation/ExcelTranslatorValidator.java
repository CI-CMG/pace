package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.object.ExcelTranslatorField;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class ExcelTranslatorValidator extends BaseValidator<ExcelTranslator> {

  @Override
  protected Set<ConstraintViolation> runValidation(ExcelTranslator object) {
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
        ExcelTranslatorField field = object.getFields().get(i);

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
        
        long sheet = field.getSheetNumber();
        if (sheet <= 0) {
          violations.add(new ConstraintViolation(
              String.format("fields[%s].sheetNumber", i), "sheetNumber must be greater than 0"
          ));
        }
      }

      if (object.getFields().size() != object.getFields().stream().map(ExcelTranslatorField::getPropertyName).distinct().count()) {
        violations.add(new ConstraintViolation(
            "fields", "fields contain duplicate property names"
        ));
      }

      if (object.getFields().size() != object.getFields().stream().map(f -> String.format("%s_%s", f.getColumnNumber(), f.getSheetNumber())).distinct().count()) {
        violations.add(new ConstraintViolation(
            "fields", "fields contain duplicate column/sheet number pairs"
        ));
      }
    }

    return violations;
  }
}

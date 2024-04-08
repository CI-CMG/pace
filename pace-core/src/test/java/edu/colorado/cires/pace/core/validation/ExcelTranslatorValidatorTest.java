package edu.colorado.cires.pace.core.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.ExcelTranslator;
import edu.colorado.cires.pace.data.ExcelTranslatorField;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class ExcelTranslatorValidatorTest {

  private final Validator<ExcelTranslator> validator = new ExcelTranslatorValidator();

  @Test
  void testEmptyObject() {
    ExcelTranslator translation = new ExcelTranslator();

    Set<ConstraintViolation> violations = validator.validate(translation);
    assertEquals(2, violations.size());

    ConstraintViolation nameViolation = violations.stream()
        .filter(cv -> cv.property().equals("name"))
        .findFirst().orElseThrow(
            () -> new IllegalStateException("name violation not found")
        );
    assertEquals("name", nameViolation.property());
    assertEquals("name must not be blank", nameViolation.message());

    ConstraintViolation fieldsViolation = violations.stream()
        .filter(cv -> cv.property().equals("fields"))
        .findFirst().orElseThrow(
            () -> new IllegalStateException("fields violation not found")
        );
    assertEquals("fields", fieldsViolation.property());
    assertEquals("fields must not be empty", fieldsViolation.message());
  }

  @Test
  void testBlankName() {
    ExcelTranslator translation = new ExcelTranslator();
    translation.setName("");

    ExcelTranslatorField field = new ExcelTranslatorField();
    field.setPropertyName("property1");
    field.setColumnNumber(1);
    field.setSheetNumber(1);
    translation.setFields(Collections.singletonList(
        field
    ));

    Set<ConstraintViolation> violations = validator.validate(translation);
    assertEquals(1, violations.size());
    ConstraintViolation constraintViolation = violations.iterator().next();
    assertEquals("name", constraintViolation.property());
    assertEquals("name must not be blank", constraintViolation.message());
  }

  @Test
  void testEmptyFields() {
    ExcelTranslator translation = new ExcelTranslator();
    translation.setFields(Collections.emptyList());
    translation.setName("name");

    Set<ConstraintViolation> violations = validator.validate(translation);
    assertEquals(1, violations.size());
    ConstraintViolation constraintViolation = violations.iterator().next();
    assertEquals("fields", constraintViolation.property());
    assertEquals("fields must not be empty", constraintViolation.message());
  }

  @Test
  void testInvalidFieldColumnNumber() {
    ExcelTranslator translation = new ExcelTranslator();
    translation.setName("name");
    ExcelTranslatorField field = new ExcelTranslatorField();
    field.setColumnNumber(-1);
    field.setPropertyName("test");
    field.setSheetNumber(1);
    translation.setFields(Collections.singletonList(field));

    Set<ConstraintViolation> violations = validator.validate(translation);
    assertEquals(1, violations.size());
    ConstraintViolation constraintViolation = violations.iterator().next();
    assertEquals("fields[0].columnNumber", constraintViolation.property());
    assertEquals("columnNumber must be greater than 0", constraintViolation.message());
  }

  @Test
  void testInvalidFieldSheetNumber() {
    ExcelTranslator translation = new ExcelTranslator();
    translation.setName("name");
    ExcelTranslatorField field = new ExcelTranslatorField();
    field.setColumnNumber(1);
    field.setPropertyName("test");
    field.setSheetNumber(-1);
    translation.setFields(Collections.singletonList(field));

    Set<ConstraintViolation> violations = validator.validate(translation);
    assertEquals(1, violations.size());
    ConstraintViolation constraintViolation = violations.iterator().next();
    assertEquals("fields[0].sheetNumber", constraintViolation.property());
    assertEquals("sheetNumber must be greater than 0", constraintViolation.message());
  }

  @Test
  void testBlankFieldName() {
    ExcelTranslator translation = new ExcelTranslator();
    translation.setName("name");
    ExcelTranslatorField field = new ExcelTranslatorField();
    field.setColumnNumber(1);
    field.setSheetNumber(1);
    translation.setFields(Collections.singletonList(field));
    Set<ConstraintViolation> violations = validator.validate(translation);
    assertEquals(1, violations.size());
    ConstraintViolation constraintViolation = violations.iterator().next();
    assertEquals("fields[0].propertyName", constraintViolation.property());
    assertEquals("propertyName must not be blank", constraintViolation.message());
  }

  @Test
  void testDuplicateColumnSheetPairs() {
    ExcelTranslator translation = new ExcelTranslator();
    translation.setName("name");

    ExcelTranslatorField field1 = new ExcelTranslatorField();
    field1.setColumnNumber(1);
    field1.setPropertyName("test1");
    field1.setSheetNumber(1);

    ExcelTranslatorField field2 = new ExcelTranslatorField();
    field2.setColumnNumber(1);
    field2.setPropertyName("test2");
    field2.setSheetNumber(1);

    translation.setFields(List.of(
        field1, field2
    ));

    Set<ConstraintViolation> violations = validator.validate(translation);
    assertEquals(1, violations.size());
    ConstraintViolation constraintViolation = violations.iterator().next();
    assertEquals("fields", constraintViolation.property());
    assertEquals("fields contain duplicate column/sheet number pairs", constraintViolation.message());
  }

  @Test
  void testDuplicatePropertyName() {
    ExcelTranslator translation = new ExcelTranslator();
    translation.setName("name");

    ExcelTranslatorField field1 = new ExcelTranslatorField();
    field1.setColumnNumber(1);
    field1.setSheetNumber(1);
    field1.setPropertyName("test1");

    ExcelTranslatorField field2 = new ExcelTranslatorField();
    field2.setColumnNumber(2);
    field2.setSheetNumber(1);
    field2.setPropertyName("test1");

    translation.setFields(List.of(
        field1, field2
    ));

    Set<ConstraintViolation> violations = validator.validate(translation);
    assertEquals(1, violations.size());

    ConstraintViolation constraintViolation = violations.iterator().next();
    assertEquals("fields", constraintViolation.property());
    assertEquals("fields contain duplicate property names", constraintViolation.message());
  }

}

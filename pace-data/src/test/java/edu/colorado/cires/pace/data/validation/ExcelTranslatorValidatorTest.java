package edu.colorado.cires.pace.data.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.object.ExcelTranslatorField;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ExcelTranslatorValidatorTest {

  private final BaseValidator<ExcelTranslator> validator = new ExcelTranslatorValidator();

  @Test
  void testEmptyObject() {
    ExcelTranslator translation = ExcelTranslator.builder().build();

    Set<ConstraintViolation> violations = validator.runValidation(translation);
    assertEquals(2, violations.size());

    ConstraintViolation nameViolation = violations.stream()
        .filter(cv -> cv.getProperty().equals("name"))
        .findFirst().orElseThrow(
            () -> new IllegalStateException("name violation not found")
        );
    assertEquals("name", nameViolation.getProperty());
    assertEquals("name must not be blank", nameViolation.getMessage());

    ConstraintViolation fieldsViolation = violations.stream()
        .filter(cv -> cv.getProperty().equals("fields"))
        .findFirst().orElseThrow(
            () -> new IllegalStateException("fields violation not found")
        );
    assertEquals("fields", fieldsViolation.getProperty());
    assertEquals("fields must not be empty", fieldsViolation.getMessage());
  }

  @Test
  void testBlankName() {
    ExcelTranslatorField field = ExcelTranslatorField.builder()
        .propertyName("property1")
        .columnNumber(1)
        .sheetNumber(1)
        .build();
    
    ExcelTranslator translation = ExcelTranslator.builder()
        .name("")
        .fields(Collections.singletonList(
            field
        )).build();

    Set<ConstraintViolation> violations = validator.runValidation(translation);
    assertEquals(1, violations.size());
    ConstraintViolation constraintViolation = violations.iterator().next();
    assertEquals("name", constraintViolation.getProperty());
    assertEquals("name must not be blank", constraintViolation.getMessage());
  }

  @Test
  void testEmptyFields() {
    ExcelTranslator translation = ExcelTranslator.builder()
        .name("name")
        .fields(Collections.emptyList())
        .build();

    Set<ConstraintViolation> violations = validator.runValidation(translation);
    assertEquals(1, violations.size());
    ConstraintViolation constraintViolation = violations.iterator().next();
    assertEquals("fields", constraintViolation.getProperty());
    assertEquals("fields must not be empty", constraintViolation.getMessage());
  }

  @Test
  void testInvalidFieldColumnNumber() {
    ExcelTranslatorField field = ExcelTranslatorField.builder()
        .propertyName("test")
        .columnNumber(-1)
        .sheetNumber(1)
        .build();
    ExcelTranslator translation = ExcelTranslator.builder()
        .name("name")
        .fields(Collections.singletonList(
            field
        )).build();

    Set<ConstraintViolation> violations = validator.runValidation(translation);
    assertEquals(1, violations.size());
    ConstraintViolation constraintViolation = violations.iterator().next();
    assertEquals("fields[0].columnNumber", constraintViolation.getProperty());
    assertEquals("columnNumber must be greater than 0", constraintViolation.getMessage());
  }

  @Test
  void testInvalidFieldSheetNumber() {
    ExcelTranslatorField field = ExcelTranslatorField.builder()
        .propertyName("test")
        .columnNumber(1)
        .sheetNumber(-1)
        .build();
    ExcelTranslator translation = ExcelTranslator.builder()
        .name("name")
        .fields(Collections.singletonList(
            field
        )).build();

    Set<ConstraintViolation> violations = validator.runValidation(translation);
    assertEquals(1, violations.size());
    ConstraintViolation constraintViolation = violations.iterator().next();
    assertEquals("fields[0].sheetNumber", constraintViolation.getProperty());
    assertEquals("sheetNumber must be greater than 0", constraintViolation.getMessage());
  }

  @Test
  void testBlankFieldName() {
    ExcelTranslatorField field = ExcelTranslatorField.builder()
        .columnNumber(1)
        .sheetNumber(1)
        .build();
    ExcelTranslator translation = ExcelTranslator.builder()
        .name("name")
        .fields(Collections.singletonList(
            field
        )).build();
    Set<ConstraintViolation> violations = validator.runValidation(translation);
    assertEquals(1, violations.size());
    ConstraintViolation constraintViolation = violations.iterator().next();
    assertEquals("fields[0].propertyName", constraintViolation.getProperty());
    assertEquals("propertyName must not be blank", constraintViolation.getMessage());
  }

  @Test
  void testDuplicateColumnSheetPairs() {
    ExcelTranslatorField field1 = ExcelTranslatorField.builder()
        .propertyName("test1")
        .columnNumber(1)
        .sheetNumber(1)
        .build();

    ExcelTranslatorField field2 = ExcelTranslatorField.builder()
        .propertyName("test2")
        .columnNumber(1)
        .sheetNumber(1)
        .build();
    
    ExcelTranslator translation = ExcelTranslator.builder()
        .name("name")
        .fields(List.of(
            field1, field2
        ))
        .build();

    Set<ConstraintViolation> violations = validator.runValidation(translation);
    assertEquals(1, violations.size());
    ConstraintViolation constraintViolation = violations.iterator().next();
    assertEquals("fields", constraintViolation.getProperty());
    assertEquals("fields contain duplicate column/sheet number pairs", constraintViolation.getMessage());
  }

  @Test
  void testDuplicatePropertyName() {
    ExcelTranslatorField field1 = ExcelTranslatorField.builder()
        .propertyName("test1")
        .columnNumber(1)
        .sheetNumber(1)
        .build();

    ExcelTranslatorField field2 = ExcelTranslatorField.builder()
        .propertyName("test1")
        .columnNumber(2)
        .sheetNumber(1)
        .build();
    
    ExcelTranslator translation = ExcelTranslator.builder()
        .name("name")
        .fields(List.of(
            field1, field2
        )).build();

    Set<ConstraintViolation> violations = validator.runValidation(translation);
    assertEquals(1, violations.size());

    ConstraintViolation constraintViolation = violations.iterator().next();
    assertEquals("fields", constraintViolation.getProperty());
    assertEquals("fields contain duplicate property names", constraintViolation.getMessage());
  }

}

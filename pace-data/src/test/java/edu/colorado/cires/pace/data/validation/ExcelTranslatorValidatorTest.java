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
    assertEquals("name", constraintViolation.property());
    assertEquals("name must not be blank", constraintViolation.message());
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
    assertEquals("fields", constraintViolation.property());
    assertEquals("fields must not be empty", constraintViolation.message());
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
    assertEquals("fields[0].columnNumber", constraintViolation.property());
    assertEquals("columnNumber must be greater than 0", constraintViolation.message());
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
    assertEquals("fields[0].sheetNumber", constraintViolation.property());
    assertEquals("sheetNumber must be greater than 0", constraintViolation.message());
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
    assertEquals("fields[0].propertyName", constraintViolation.property());
    assertEquals("propertyName must not be blank", constraintViolation.message());
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
    assertEquals("fields", constraintViolation.property());
    assertEquals("fields contain duplicate column/sheet number pairs", constraintViolation.message());
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
    assertEquals("fields", constraintViolation.property());
    assertEquals("fields contain duplicate property names", constraintViolation.message());
  }

}

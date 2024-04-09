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
    ExcelTranslator translation = new ExcelTranslator(
        null,
        null,
        null
    );

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
    ExcelTranslatorField field = new ExcelTranslatorField(
        "property1",
        1,
        1
    );
    ExcelTranslator translation = new ExcelTranslator(
        null,
        "",
        Collections.singletonList(
            field
        )
    );

    Set<ConstraintViolation> violations = validator.validate(translation);
    assertEquals(1, violations.size());
    ConstraintViolation constraintViolation = violations.iterator().next();
    assertEquals("name", constraintViolation.property());
    assertEquals("name must not be blank", constraintViolation.message());
  }

  @Test
  void testEmptyFields() {
    ExcelTranslator translation = new ExcelTranslator(
        null,
        "name",
        Collections.emptyList()
    );

    Set<ConstraintViolation> violations = validator.validate(translation);
    assertEquals(1, violations.size());
    ConstraintViolation constraintViolation = violations.iterator().next();
    assertEquals("fields", constraintViolation.property());
    assertEquals("fields must not be empty", constraintViolation.message());
  }

  @Test
  void testInvalidFieldColumnNumber() {
    ExcelTranslatorField field = new ExcelTranslatorField(
        "test",
        -1,
        1
    );
    ExcelTranslator translation = new ExcelTranslator(
        null,
        "name",
        Collections.singletonList(
            field
        )
    );

    Set<ConstraintViolation> violations = validator.validate(translation);
    assertEquals(1, violations.size());
    ConstraintViolation constraintViolation = violations.iterator().next();
    assertEquals("fields[0].columnNumber", constraintViolation.property());
    assertEquals("columnNumber must be greater than 0", constraintViolation.message());
  }

  @Test
  void testInvalidFieldSheetNumber() {
    ExcelTranslatorField field = new ExcelTranslatorField(
        "test",
        1,
        -1
    );
    ExcelTranslator translation = new ExcelTranslator(
        null,
        "name",
        Collections.singletonList(
            field
        )
    );

    Set<ConstraintViolation> violations = validator.validate(translation);
    assertEquals(1, violations.size());
    ConstraintViolation constraintViolation = violations.iterator().next();
    assertEquals("fields[0].sheetNumber", constraintViolation.property());
    assertEquals("sheetNumber must be greater than 0", constraintViolation.message());
  }

  @Test
  void testBlankFieldName() {
    ExcelTranslatorField field = new ExcelTranslatorField(
        null,
        1,
        1
    );
    ExcelTranslator translation = new ExcelTranslator(
        null,
        "name",
        Collections.singletonList(
            field
        )
    );
    Set<ConstraintViolation> violations = validator.validate(translation);
    assertEquals(1, violations.size());
    ConstraintViolation constraintViolation = violations.iterator().next();
    assertEquals("fields[0].propertyName", constraintViolation.property());
    assertEquals("propertyName must not be blank", constraintViolation.message());
  }

  @Test
  void testDuplicateColumnSheetPairs() {
    ExcelTranslatorField field1 = new ExcelTranslatorField(
        "test1",
        1,
        1
    );

    ExcelTranslatorField field2 = new ExcelTranslatorField(
        "test2",
        1,
        1
    );
    
    ExcelTranslator translation = new ExcelTranslator(
        null,
        "name",
        List.of(
            field1, field2
        )
    );

    Set<ConstraintViolation> violations = validator.validate(translation);
    assertEquals(1, violations.size());
    ConstraintViolation constraintViolation = violations.iterator().next();
    assertEquals("fields", constraintViolation.property());
    assertEquals("fields contain duplicate column/sheet number pairs", constraintViolation.message());
  }

  @Test
  void testDuplicatePropertyName() {
    ExcelTranslatorField field1 = new ExcelTranslatorField(
        "test1",
        1,
        1
    );

    ExcelTranslatorField field2 = new ExcelTranslatorField(
        "test1",
        2,
        1
    );
    
    ExcelTranslator translation = new ExcelTranslator(
        null,
        "name",
        List.of(
            field1, field2
        )
    );

    Set<ConstraintViolation> violations = validator.validate(translation);
    assertEquals(1, violations.size());

    ConstraintViolation constraintViolation = violations.iterator().next();
    assertEquals("fields", constraintViolation.property());
    assertEquals("fields contain duplicate property names", constraintViolation.message());
  }

}

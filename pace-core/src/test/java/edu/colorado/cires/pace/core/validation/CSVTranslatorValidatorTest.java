package edu.colorado.cires.pace.core.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.CSVTranslator;
import edu.colorado.cires.pace.data.CSVTranslatorField;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class CSVTranslatorValidatorTest {
  
  private final Validator<CSVTranslator> validator = new CSVTranslatorValidator();
  
  @Test
  void testEmptyObject() {
    CSVTranslator translation = new CSVTranslator(null, null, null);

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
    CSVTranslatorField field = new CSVTranslatorField(
        "property1",
        1
    );
    CSVTranslator translation = new CSVTranslator(
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
    CSVTranslator translation = new CSVTranslator(
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
    CSVTranslatorField field = new CSVTranslatorField(
        "test",
        -1
    );
    CSVTranslator translation = new CSVTranslator(
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
  void testBlankFieldName() {
    CSVTranslatorField field = new CSVTranslatorField(
        null,
        1
    );
    CSVTranslator translation = new CSVTranslator(
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
  void testDuplicateColumnNumber() {
    CSVTranslatorField field1 = new CSVTranslatorField(
        "test1",
        1
    );
    
    CSVTranslatorField field2 = new CSVTranslatorField(
        "test2",
        1
    );
    CSVTranslator translation = new CSVTranslator(
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
    assertEquals("fields contain duplicate column numbers", constraintViolation.message());
  }
  
  @Test
  void testDuplicatePropertyName() {
    CSVTranslatorField field1 = new CSVTranslatorField(
        "test1",
        1
    );
    CSVTranslatorField field2 = new CSVTranslatorField(
        "test1",
        2
    );
    
    CSVTranslator translation = new CSVTranslator(
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

package edu.colorado.cires.pace.data.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.data.object.CSVTranslatorField;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CSVTranslatorValidatorTest {
  
  private final BaseValidator<CSVTranslator> validator = new CSVTranslatorValidator();
  
  @Test
  void testEmptyObject() {
    CSVTranslator translation = CSVTranslator.builder().build();

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
    CSVTranslatorField field = CSVTranslatorField.builder()
        .propertyName("property1")
        .columnNumber(1)
        .build();
    CSVTranslator translation = CSVTranslator.builder()
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
    CSVTranslator translation = CSVTranslator.builder()
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
    CSVTranslatorField field = CSVTranslatorField.builder()
        .propertyName("test")
        .columnNumber(-1)
        .build();
    
    CSVTranslator translation = CSVTranslator.builder()
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
  void testBlankFieldName() {
    CSVTranslatorField field = CSVTranslatorField.builder()
        .columnNumber(1)
        .build(); 
    
    CSVTranslator translation = CSVTranslator.builder()
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
  void testDuplicateColumnNumber() {
    CSVTranslatorField field1 = CSVTranslatorField.builder()
        .propertyName("test1")
        .columnNumber(1)
        .build();
    
    CSVTranslatorField field2 = CSVTranslatorField.builder()
        .propertyName("test2")
        .columnNumber(1)
        .build();
    
    CSVTranslator translation = CSVTranslator.builder()
        .name("name")
        .fields(List.of(
            field1, field2
        )).build();
    
    Set<ConstraintViolation> violations = validator.runValidation(translation);
    assertEquals(1, violations.size());
    ConstraintViolation constraintViolation = violations.iterator().next();
    assertEquals("fields", constraintViolation.property());
    assertEquals("fields contain duplicate column numbers", constraintViolation.message());
  }
  
  @Test
  void testDuplicatePropertyName() {
    CSVTranslatorField field1 = CSVTranslatorField.builder()
        .propertyName("test1")
        .columnNumber(1)
        .build();
    
    CSVTranslatorField field2 = CSVTranslatorField.builder()
        .propertyName("test1")
        .columnNumber(2)
        .build();
    
    CSVTranslator translation = CSVTranslator.builder()
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

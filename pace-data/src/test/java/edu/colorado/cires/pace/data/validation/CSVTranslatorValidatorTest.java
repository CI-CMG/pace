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
    assertEquals("name", constraintViolation.getProperty());
    assertEquals("name must not be blank", constraintViolation.getMessage());
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
    assertEquals("fields", constraintViolation.getProperty());
    assertEquals("fields must not be empty", constraintViolation.getMessage());
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
    assertEquals("fields[0].columnNumber", constraintViolation.getProperty());
    assertEquals("columnNumber must be greater than 0", constraintViolation.getMessage());
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
    assertEquals("fields[0].propertyName", constraintViolation.getProperty());
    assertEquals("propertyName must not be blank", constraintViolation.getMessage());
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
    assertEquals("fields", constraintViolation.getProperty());
    assertEquals("fields contain duplicate column numbers", constraintViolation.getMessage());
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
    assertEquals("fields", constraintViolation.getProperty());
    assertEquals("fields contain duplicate property names", constraintViolation.getMessage());
  }

}

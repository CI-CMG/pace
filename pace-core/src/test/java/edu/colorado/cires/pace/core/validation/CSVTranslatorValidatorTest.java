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
    CSVTranslator translation = new CSVTranslator();

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
    CSVTranslator translation = new CSVTranslator();
    translation.setName("");

    CSVTranslatorField field = new CSVTranslatorField();
    field.setPropertyName("property1");
    field.setColumnNumber(1);
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
    CSVTranslator translation = new CSVTranslator();
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
    CSVTranslator translation = new CSVTranslator();
    translation.setName("name");
    CSVTranslatorField field = new CSVTranslatorField();
    field.setColumnNumber(-1);
    field.setPropertyName("test");
    translation.setFields(Collections.singletonList(field));
    
    Set<ConstraintViolation> violations = validator.validate(translation);
    assertEquals(1, violations.size());
    ConstraintViolation constraintViolation = violations.iterator().next();
    assertEquals("fields[0].columnNumber", constraintViolation.property());
    assertEquals("columnNumber must be greater than 0", constraintViolation.message());
  }
  
  @Test
  void testBlankFieldName() {
    CSVTranslator translation = new CSVTranslator();
    translation.setName("name");
    CSVTranslatorField field = new CSVTranslatorField();
    field.setColumnNumber(1);
    translation.setFields(Collections.singletonList(field));
    Set<ConstraintViolation> violations = validator.validate(translation);
    assertEquals(1, violations.size());
    ConstraintViolation constraintViolation = violations.iterator().next();
    assertEquals("fields[0].propertyName", constraintViolation.property());
    assertEquals("propertyName must not be blank", constraintViolation.message());
  }
  
  @Test
  void testDuplicateColumnNumber() {
    CSVTranslator translation = new CSVTranslator();
    translation.setName("name");
    
    CSVTranslatorField field1 = new CSVTranslatorField();
    field1.setColumnNumber(1);
    field1.setPropertyName("test1");
    
    CSVTranslatorField field2 = new CSVTranslatorField();
    field2.setColumnNumber(1);
    field2.setPropertyName("test2");
    
    translation.setFields(List.of(
        field1, field2
    ));
    
    Set<ConstraintViolation> violations = validator.validate(translation);
    assertEquals(1, violations.size());
    ConstraintViolation constraintViolation = violations.iterator().next();
    assertEquals("fields", constraintViolation.property());
    assertEquals("fields contain duplicate column numbers", constraintViolation.message());
  }
  
  @Test
  void testDuplicatePropertyName() {
    CSVTranslator translation = new CSVTranslator();
    translation.setName("name");
    
    CSVTranslatorField field1 = new CSVTranslatorField();
    field1.setColumnNumber(1);
    field1.setPropertyName("test1");
    
    CSVTranslatorField field2 = new CSVTranslatorField();
    field2.setColumnNumber(2);
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

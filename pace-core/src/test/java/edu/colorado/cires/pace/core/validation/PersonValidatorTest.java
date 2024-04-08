package edu.colorado.cires.pace.core.validation;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.Person;
import java.util.Set;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.platform.commons.util.StringUtils;

class PersonValidatorTest {
  
  private static final Validator<Person> validator = new PersonValidator();

  @ParameterizedTest
  @CsvSource(value = {
      "name,org,position,true",
      ",org,position,false",
      "name,,position,false",
      "name,org,,false",
  })
  void validate(String name, String org, String position, boolean expectedPass) {
    Person person = new Person();
    person.setName(name);
    person.setOrganization(org);
    person.setPosition(position);
    
    Set<ConstraintViolation> violations = validator.validate(person);
    assertEquals(expectedPass, violations.isEmpty());
    
    if (expectedPass) {
      return;
    }
    
    assertEquals(1, violations.size());
    ConstraintViolation violation = violations.iterator().next();
    
    if (StringUtils.isBlank(name)) {
      assertEquals("name", violation.property());
      assertEquals("name must not be blank", violation.message());
    } else if (StringUtils.isBlank(org)) {
      assertEquals("organization", violation.property());
      assertEquals("organization must not be blank", violation.message());
    } else if (StringUtils.isBlank(position)) {
      assertEquals("position", violation.property());
      assertEquals("position must not be blank", violation.message());
    }
  }
}
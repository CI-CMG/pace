package edu.colorado.cires.pace.data.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.colorado.cires.pace.data.object.Person;
import java.util.Set;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.platform.commons.util.StringUtils;

class PersonValidatorTest {

  @ParameterizedTest
  @CsvSource(value = {
      "name,org,position,true",
      ",org,position,false",
      "name,,position,false",
      "name,org,,false",
  })
  void validate(String name, String org, String position, boolean expectedPass) {
    if (expectedPass) {
      assertDoesNotThrow(() -> Person.builder()
          .name(name)
          .organization(org)
          .position(position)
          .build());
      return;
    } else {
      ValidationException exception = assertThrows(ValidationException.class, () -> Person.builder()
          .name(name)
          .organization(org)
          .position(position)
          .build());

      Set<ConstraintViolation> violations = exception.getViolations();
      assertEquals(expectedPass, violations.isEmpty());

      assertEquals(1, violations.size());
      ConstraintViolation violation = violations.iterator().next();

      if (StringUtils.isBlank(name)) {
        assertEquals("name", violation.getProperty());
        assertEquals("name must not be blank", violation.getMessage());
      } else if (StringUtils.isBlank(org)) {
        assertEquals("organization", violation.getProperty());
        assertEquals("organization must not be blank", violation.getMessage());
      } else if (StringUtils.isBlank(position)) {
        assertEquals("position", violation.getProperty());
        assertEquals("position must not be blank", violation.getMessage());
      }
    }
  }
}
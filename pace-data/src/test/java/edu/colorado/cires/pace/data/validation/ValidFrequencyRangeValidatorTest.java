package edu.colorado.cires.pace.data.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.FrequencyRange;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ValidFrequencyRangeValidatorTest {
  
  @ParameterizedTest
  @CsvSource(value = {
      "1.0,1.0,,",
      "1.0,2.0,,",
      ",1.0,must not be null,",
      "1.0,,,must not be null",
      ",,must not be null,must not be null",
      "2.0,1.0,must be less than or equal to maxFrequency,must be greater than or equal to minFrequency"
  })
  void testValidFrequencyRange(Float minFrequency, Float maxFrequency, String expectedMinMessage, String expectedMaxMessage) {
    FrequencyRange frequencyRange = new FrequencyRange() {
      @Override
      public Float getMinFrequency() {
        return minFrequency;
      }

      @Override
      public Float getMaxFrequency() {
        return maxFrequency;
      }
    };

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    Set<ConstraintViolation<FrequencyRange>> violations = validator.validate(frequencyRange);
    
    if (expectedMinMessage == null && expectedMaxMessage == null) {
      assertTrue(violations.isEmpty());
    }
    
    if (expectedMinMessage != null) {
      ConstraintViolation<FrequencyRange> violation = violations.stream()
          .filter(v -> v.getPropertyPath().toString().equals("minFrequency"))
          .findFirst().orElseThrow();
      
      assertEquals(expectedMinMessage, violation.getMessage());
    }
    
    if (expectedMaxMessage != null) {
      ConstraintViolation<FrequencyRange> violation = violations.stream()
          .filter(v -> v.getPropertyPath().toString().equals("maxFrequency"))
          .findFirst().orElseThrow();

      assertEquals(expectedMaxMessage, violation.getMessage());
    }
  }

}

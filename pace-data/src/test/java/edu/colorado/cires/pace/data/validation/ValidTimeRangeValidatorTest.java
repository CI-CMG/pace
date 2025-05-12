package edu.colorado.cires.pace.data.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.TimeRange;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ValidTimeRangeValidatorTest {
  
  @ParameterizedTest
  @CsvSource(value = {
      "2024-12-03T10:15:30,2024-12-04T10:15:30,,",
      "2024-12-03T10:15:30,2024-12-03T10:15:30,must not equal endTime,must not equal startTime",
      "2024-12-04T10:15:30,2024-12-03T10:15:30,must be before endTime,must be after startTime",
      "2024-12-04T10:15:30,,,must not be null",
      ",2024-12-04T10:15:30,must not be null,",
      ",,must not be null,must not be null"
  })
  void testValidTimeRange(String startString, String endString, String expectedStartMessage, String expectedEndMessage) {
    TimeRange timeRange = new TimeRange() {
      @Override
      public LocalDateTime getStartTime() {
        return startString == null ? null : LocalDateTime.parse(startString);
      }

      @Override
      public LocalDateTime getEndTime() {
        return endString == null ? null : LocalDateTime.parse(endString);
      }
    };


    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    Set<ConstraintViolation<TimeRange>> violations = validator.validate(timeRange);
    
    if (expectedStartMessage == null && expectedEndMessage == null) {
      assertTrue(violations.isEmpty());
    } else if (expectedStartMessage != null) {
      ConstraintViolation<TimeRange> violation = violations.stream()
          .filter(v -> v.getPropertyPath().toString().equals("startTime"))
          .findFirst().orElseThrow();
      assertEquals(expectedStartMessage, violation.getMessage());
    } else {
      ConstraintViolation<TimeRange> violation = violations.stream()
          .filter(v -> v.getPropertyPath().toString().equals("endTime"))
          .findFirst().orElseThrow();
      assertEquals(expectedEndMessage, violation.getMessage());
    }
  }

}

package edu.colorado.cires.pace.data.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.pace.data.object.ElevationPair;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ValidElevationPairValidatorTest {
  
  @ParameterizedTest
  @CsvSource(value = {
      "10.0,20.0,,",
      "10.0,10.0,,",
      ",,must not be null,must not be null",
      "10.0,,,must not be null",
      ",10.0,must not be null,",
      "10.0,5.0,must be less than or equal to instrumentElevation,must be greater than or equal to surfaceElevation"
  })
  void testValidElevationPair(Float surfaceElevation, Float instrumentElevation, String expectedSurfaceMessage, String expectedInstrumentMessage) {
    ElevationPair elevationPair = new ElevationPair() {
      @Override
      public Float getSurfaceElevation() {
        return surfaceElevation;
      }

      @Override
      public Float getInstrumentElevation() {
        return instrumentElevation;
      }
    };

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    Set<ConstraintViolation<ElevationPair>> violations = validator.validate(elevationPair);
    
    if (expectedInstrumentMessage == null && expectedSurfaceMessage == null) {
      assertTrue(violations.isEmpty());
    }
    
    if (expectedInstrumentMessage != null) {
      ConstraintViolation<ElevationPair> violation = violations.stream()
          .filter(v -> v.getPropertyPath().toString().equals("instrumentElevation"))
          .findFirst().orElseThrow();
      assertEquals(expectedInstrumentMessage, violation.getMessage());
    }
    
    if (expectedSurfaceMessage != null) {
      ConstraintViolation<ElevationPair> violation = violations.stream()
          .filter(v -> v.getPropertyPath().toString().equals("surfaceElevation"))
          .findFirst().orElseThrow();
      assertEquals(expectedSurfaceMessage, violation.getMessage());
    }
  }

}

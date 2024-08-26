package edu.colorado.cires.pace.data.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MarineInstrumentLocation;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ValidMarineInstrumentLocationValidatorTest {
  
  @ParameterizedTest
  @CsvSource(value = {
      "-10.0,-5.0,,",
      "-10.0,-10.0,,",
      ",,must not be null,must not be null",
      "-10.0,,,must not be null",
      ",-10.0,must not be null,",
      "-5.0,-10.0,must be less than or equal to instrumentDepth,must be greater than or equal to seaFloorDepth"
  })
  void testValidMarineInstrumentLocation(Float seaFloorDepth, Float instrumentDepth, String expectedSeaFloorDepthMessage, String expectedInstrumentDepthMessage) {
    MarineInstrumentLocation marineInstrumentLocation = MarineInstrumentLocation.builder()
        .instrumentDepth(instrumentDepth)
        .seaFloorDepth(seaFloorDepth)
        .latitude(10d)
        .longitude(10d)
        .build();

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    Set<ConstraintViolation<MarineInstrumentLocation>> violations = validator.validate(marineInstrumentLocation);
    
    if (expectedInstrumentDepthMessage == null && expectedSeaFloorDepthMessage == null) {
      assertTrue(violations.isEmpty());
    }
    
    if (expectedInstrumentDepthMessage != null) {
      ConstraintViolation<MarineInstrumentLocation> violation = violations.stream()
          .filter(v -> v.getPropertyPath().toString().equals("instrumentDepth"))
          .findFirst().orElseThrow();
      assertEquals(expectedInstrumentDepthMessage, violation.getMessage());
    }
  }

}

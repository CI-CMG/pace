package edu.colorado.cires.pace.data.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.CalibrationDetail;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ValidCalibrationDetailValidatorTest {
  
  @ParameterizedTest
  @CsvSource(value = {
      "2024-12-03,2024-12-03,,",
      "2024-12-03,2024-12-10,,",
      "2024-12-15,2024-12-10,must be before or equal to postDeploymentCalibrationDate,must be after or equal to preDeploymentCalibrationDate",
      ",,,",
      ",2024-12-10,,",
      "2024-12-03,,,",
  })
  void testValidCalibrationDetail(String preString, String postString, String expectedPreMessage, String expectedPostMessage) {
    CalibrationDetail calibrationDetail = new CalibrationDetail() {
      @Override
      public LocalDate getPreDeploymentCalibrationDate() {
        return preString == null ? null : LocalDate.parse(preString);
      }

      @Override
      public LocalDate getPostDeploymentCalibrationDate() {
        return postString == null ? null : LocalDate.parse(postString);
      }

      @Override
      public String getCalibrationDescription() {
        return "";
      }
    };

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    Set<ConstraintViolation<CalibrationDetail>> violations = validator.validate(calibrationDetail);
    
    if (expectedPostMessage == null && expectedPreMessage == null) {
      assertTrue(violations.isEmpty());
    }
    
    if (expectedPreMessage != null) {
      ConstraintViolation<CalibrationDetail> violation = violations.stream()
          .filter(v -> v.getPropertyPath().toString().equals("preDeploymentCalibrationDate"))
          .findFirst().orElseThrow();
      assertEquals(expectedPreMessage, violation.getMessage());
    }
    
    if (expectedPostMessage != null) {
      ConstraintViolation<CalibrationDetail> violation = violations.stream()
          .filter(v -> v.getPropertyPath().toString().equals("postDeploymentCalibrationDate"))
          .findFirst().orElseThrow();
      assertEquals(expectedPostMessage, violation.getMessage());
    }
  }

}

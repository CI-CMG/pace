package edu.colorado.cires.pace.data.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.colorado.cires.pace.data.object.PackingJob;
import java.nio.file.Path;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PackingJobValidatorTest {

  @ParameterizedTest
  @CsvSource(value = {
      "sourcePath,/path/to/files,true,",
      "sourcePath,path/to/files,false,sourcePath must be an absolute path",
      "sourcePath,'',false,sourcePath must not be blank",
      "biologicalPath,/path/to/files,true,",
      "biologicalPath,path/to/files,false,biologicalPath must be an absolute path",
      "calibrationDocumentsPath,/path/to/files,true,",
      "calibrationDocumentsPath,path/to/files,false,calibrationDocumentsPath must be an absolute path",
      "documentsPath,/path/to/files,true,",
      "documentsPath,path/to/files,false,documentsPath must be an absolute path",
      "navigationPath,/path/to/files,true,",
      "navigationPath,path/to/files,false,navigationPath must be an absolute path",
      "otherPath,/path/to/files,true,",
      "otherPath,path/to/files,false,otherPath must be an absolute path",
      "temperaturePath,/path/to/files,true,",
      "temperaturePath,path/to/files,false,temperaturePath must be an absolute path",
  })
  void test(String property, String propertyPath, boolean expectedValid, String expectedError) {
    if (expectedValid) {
      assertDoesNotThrow(() -> getPackingJob(property, propertyPath));
    } else {
      ValidationException exception = assertThrows(ValidationException.class, () -> getPackingJob(property, propertyPath));
      Set<ConstraintViolation> violations = exception.getViolations();
      ConstraintViolation constraintViolation = violations.stream().filter(cv -> cv.getProperty().equals(property))
          .findFirst().orElseThrow(
              () -> new IllegalStateException("No constraint violation found for property " + property)
          );
      assertEquals(property, constraintViolation.getProperty());
      assertEquals(expectedError, constraintViolation.getMessage());
    }
  }
  
  @Test
  void testNullSourcePath() {
    ValidationException exception = assertThrows(ValidationException.class, () -> PackingJob.builder().build());

    Set<ConstraintViolation> violations = exception.getViolations();
    assertEquals(1, violations.size());
    
    ConstraintViolation violation = violations.iterator().next();
    assertEquals("sourcePath", violation.getProperty());
    assertEquals("sourcePath must be defined", violation.getMessage());
  }

  private static PackingJob getPackingJob(String property, String propertyPath) throws ValidationException {
    return PackingJob.builder()
        .temperaturePath(returnPropertyIfMatchesArgument(property, "temperaturePath", propertyPath))
        .biologicalPath(returnPropertyIfMatchesArgument(property, "biologicalPath", propertyPath))
        .otherPath(returnPropertyIfMatchesArgument(property, "otherPath", propertyPath))
        .documentsPath(returnPropertyIfMatchesArgument(property, "documentsPath", propertyPath))
        .calibrationDocumentsPath(returnPropertyIfMatchesArgument(property, "calibrationDocumentsPath", propertyPath))
        .navigationPath(returnPropertyIfMatchesArgument(property, "navigationPath", propertyPath))
        .sourcePath(property.equals("sourcePath") ? returnPropertyIfMatchesArgument(property, "sourcePath", propertyPath) : Path.of("/path/to/source/file"))
        .build();
  }
  
  private static Path returnPropertyIfMatchesArgument(String property, String expectedProperty, String propertyPath) {
    if (property.equals(expectedProperty)) {
      return Path.of(propertyPath);
    }
    return null;
  }

}

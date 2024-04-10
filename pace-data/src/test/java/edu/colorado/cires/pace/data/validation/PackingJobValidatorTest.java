package edu.colorado.cires.pace.data.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.pace.data.object.PackingJob;
import java.nio.file.Path;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PackingJobValidatorTest {
  
  private final BaseValidator<PackingJob> validator = new PackingJobValidator();
  
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
    PackingJob packingJob = getPackingJob(property, propertyPath);

    Set<ConstraintViolation> violations = validator.runValidation(packingJob);
    if (expectedValid) {
      assertTrue(violations.isEmpty());
    } else {
      assertFalse(violations.isEmpty());
      
      ConstraintViolation constraintViolation = violations.stream().filter(cv -> cv.property().equals(property))
          .findFirst().orElseThrow(
              () -> new IllegalStateException("No constraint violation found for property " + property)
          );
      assertEquals(property, constraintViolation.property());
      assertEquals(expectedError, constraintViolation.message());
    }
  }
  
  @Test
  void testNullSourcePath() {
    PackingJob packingJob = new PackingJob(
        null,
        null,
        null,
        null,
        null,
        null,
        null
    );

    Set<ConstraintViolation> violations = validator.runValidation(packingJob);
    assertEquals(1, violations.size());
    
    ConstraintViolation violation = violations.iterator().next();
    assertEquals("sourcePath", violation.property());
    assertEquals("sourcePath must be defined", violation.message());
  }

  private static PackingJob getPackingJob(String property, String propertyPath) {
    return new PackingJob(
        returnPropertyIfMatchesArgument(property, "temperaturePath", propertyPath),
        returnPropertyIfMatchesArgument(property, "biologicalPath", propertyPath),
        returnPropertyIfMatchesArgument(property, "otherPath", propertyPath),
        returnPropertyIfMatchesArgument(property, "documentsPath", propertyPath),
        returnPropertyIfMatchesArgument(property, "calibrationDocumentsPath", propertyPath),
        returnPropertyIfMatchesArgument(property, "navigationPath", propertyPath),
        property.equals("sourcePath") ? returnPropertyIfMatchesArgument(property, "sourcePath", propertyPath) : Path.of("/path/to/source/file")
    );
  }
  
  private static Path returnPropertyIfMatchesArgument(String property, String expectedProperty, String propertyPath) {
    if (property.equals(expectedProperty)) {
      return Path.of(propertyPath);
    }
    return null;
  }

}

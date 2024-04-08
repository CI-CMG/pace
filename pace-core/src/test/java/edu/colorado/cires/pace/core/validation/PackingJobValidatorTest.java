package edu.colorado.cires.pace.core.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.pace.data.PackingJob;
import java.util.Set;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PackingJobValidatorTest {
  
  private final Validator<PackingJob> validator = new PackingJobValidator();
  
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

    Set<ConstraintViolation> violations = validator.validate(packingJob);
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

  private static PackingJob getPackingJob(String property, String propertyPath) {
    PackingJob packingJob = new PackingJob();

    if (property.equals("sourcePath")) {
      packingJob.setSourcePath(propertyPath);
    }
    if (property.equals("biologicalPath")) {
      packingJob.setBiologicalPath(propertyPath);
    }
    if (property.equals("calibrationDocumentsPath")) {
      packingJob.setCalibrationDocumentsPath(propertyPath);
    }
    if (property.equals("documentsPath")) {
      packingJob.setDocumentsPath(propertyPath);
    }
    if (property.equals("navigationPath")) {
      packingJob.setNavigationPath(propertyPath);
    }
    if (property.equals("otherPath")) {
      packingJob.setOtherPath(propertyPath);
    }
    if (property.equals("temperaturePath")) {
      packingJob.setTemperaturePath(propertyPath);
    }

    if (packingJob.getSourcePath() == null) {
      packingJob.setSourcePath("/path/to/source");
    }
    return packingJob;
  }

}

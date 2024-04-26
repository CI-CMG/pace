package edu.colorado.cires.pace.packaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.PackingJob;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public class PackageProcessor {
  
  private final ObjectMapper objectMapper;
  private final Validator validator;

  public PackageProcessor(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    this.validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  public void process(PackingJob packingJob, Path outputDir, boolean sourceDataContainsAudioFiles)
      throws PackagingException, IOException {
    validatePackingJob(packingJob);
    
    FileUtils.mkdir(outputDir);
    
    Packager.run(
        PackageInstructionFactory.getPackageInstructions(
            packingJob,
            FileUtils.writeMetadata(packingJob.getDataset(), objectMapper, outputDir.resolve("data")),
            outputDir,
            sourceDataContainsAudioFiles
        ),
        outputDir
    );
  }
  
  private void validatePackingJob(PackingJob packingJob) {
    Set<ConstraintViolation<PackingJob>> violations = validator.validate(packingJob);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(String.format(
          "%s validation failed", PackingJob.class.getSimpleName()
      ), violations);
    }
  }

}

package edu.colorado.cires.pace.packaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.Dataset;
import edu.colorado.cires.pace.data.object.Package;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Stream;

public class PackageProcessor {
  
  private final ObjectMapper objectMapper;
  private final Validator validator;

  public PackageProcessor(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    this.validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  public void process(Package packingJob, Path outputDir, ProgressIndicator... progressIndicators)
      throws PackagingException, IOException {
    validatePackingJob(packingJob);
    
    FileUtils.mkdir(outputDir);
    Path packageOutputDir = outputDir.resolve(((Dataset) packingJob).getPackageId());
    FileUtils.mkdir(packageOutputDir);

    Stream<PackageInstruction> instructionStream = PackageInstructionFactory.getPackageInstructions(
        packingJob,
        FileUtils.writeMetadata((Dataset) packingJob, objectMapper, packageOutputDir.resolve("data")),
        packageOutputDir
    );
    long totalRecords = instructionStream.count() + 4L; // accounting for generated files

    for (ProgressIndicator progressIndicator : progressIndicators) {
      progressIndicator.setTotalRecords(totalRecords);
    }

    instructionStream = PackageInstructionFactory.getPackageInstructions(
        packingJob,
        FileUtils.writeMetadata((Dataset) packingJob, objectMapper, packageOutputDir.resolve("data")),
        packageOutputDir
    );
    
    Packager.run(
        instructionStream,
        packageOutputDir,
        progressIndicators
    );
  }
  
  private void validatePackingJob(Package packingJob) {
    Set<ConstraintViolation<Package>> violations = validator.validate(packingJob);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(String.format(
          "%s validation failed", Package.class.getSimpleName()
      ), violations);
    }
  }

}

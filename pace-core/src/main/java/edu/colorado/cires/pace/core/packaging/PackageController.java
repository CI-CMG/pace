package edu.colorado.cires.pace.core.packaging;

import edu.colorado.cires.pace.core.exception.PackingException;
import edu.colorado.cires.pace.core.exception.ValidationException;
import edu.colorado.cires.pace.core.validation.ConstraintViolation;
import edu.colorado.cires.pace.core.validation.PackingJobValidator;
import edu.colorado.cires.pace.core.validation.Validator;
import edu.colorado.cires.pace.data.PackingJob;
import java.nio.file.Path;
import java.util.Set;

public class PackageController {
  
  private final Validator<PackingJob> validator;

  public PackageController() {
    this.validator = new PackingJobValidator();
  }

  public void process(PackingJob packingJob, Path outputDir, boolean sourceDataContainsAudioFiles) throws PackingException, ValidationException {
    Set<ConstraintViolation> violations = validator.validate(packingJob);
    if (!violations.isEmpty()) {
      throw new ValidationException(violations);
    }
    
    Packager.run(
        PackageInstructionFactory.getPackageInstructions(packingJob, outputDir, sourceDataContainsAudioFiles),
        outputDir
    );
  }

}

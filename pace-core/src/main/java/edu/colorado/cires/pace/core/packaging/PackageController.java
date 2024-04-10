package edu.colorado.cires.pace.core.packaging;

import edu.colorado.cires.pace.core.exception.PackingException;
import edu.colorado.cires.pace.data.validation.ValidationException;
import edu.colorado.cires.pace.data.object.PackingJob;
import java.nio.file.Path;

public class PackageController {

  public PackageController() {
  }

  public void process(PackingJob packingJob, Path outputDir, boolean sourceDataContainsAudioFiles) throws PackingException, ValidationException {
    
    Packager.run(
        PackageInstructionFactory.getPackageInstructions(packingJob, outputDir, sourceDataContainsAudioFiles),
        outputDir
    );
  }

}

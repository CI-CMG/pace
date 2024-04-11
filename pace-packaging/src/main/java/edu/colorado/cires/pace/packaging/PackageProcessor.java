package edu.colorado.cires.pace.packaging;

import edu.colorado.cires.pace.data.object.PackingJob;
import java.nio.file.Path;

public class PackageProcessor {

  public static void process(PackingJob packingJob, Path outputDir, boolean sourceDataContainsAudioFiles) throws PackagingException {
    Packager.run(
        PackageInstructionFactory.getPackageInstructions(packingJob, outputDir, sourceDataContainsAudioFiles),
        outputDir
    );
  }

}

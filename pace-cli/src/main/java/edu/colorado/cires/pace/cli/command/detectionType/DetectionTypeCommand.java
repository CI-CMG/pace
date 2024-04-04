package edu.colorado.cires.pace.cli.command.detectionType;

import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import picocli.CommandLine.Command;

@Command(
    name = "detection-type",
    description = "Manage detection types",
    mixinStandardHelpOptions = true,
    versionProvider = VersionProvider.class,
    subcommands = {
        CreateDetectionTypeCommand.class,
        DeleteDetectionTypeCommand.class,
        FindAllDetectionTypesCommand.class,
        GetDetectionTypeByScienceNameCommand.class,
        GetDetectionTypeUUIDCommand.class,
        UpdateDetectionTypeCommand.class,
    }
)
public class DetectionTypeCommand implements Runnable {

  @Override
  public void run() {
    
  }
}

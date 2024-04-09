package edu.colorado.cires.pace.cli.command.detectionType;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.DetectionType;
import picocli.CommandLine.Command;

@Command(name = "list", description = "List all detection types", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class FindAllDetectionTypesCommand extends FindAllCommand<DetectionType> {

  @Override
  protected ControllerFactory<DetectionType> getControllerFactory() {
    return DetectionTypeControllerFactory::createController;
  }
}

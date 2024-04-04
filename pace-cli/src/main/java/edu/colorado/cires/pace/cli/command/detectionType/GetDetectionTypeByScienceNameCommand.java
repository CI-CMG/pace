package edu.colorado.cires.pace.cli.command.detectionType;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.GetByUniqueFieldCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.DetectionType;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get-by-science-name", description = "Get detection type by science name", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class GetDetectionTypeByScienceNameCommand extends GetByUniqueFieldCommand<DetectionType, String> {
  
  @Parameters(description = "detection type science name")
  private String scienceName;

  @Override
  protected Supplier<String> getUniqueFieldProvider() {
    return () -> scienceName;
  }

  @Override
  protected ControllerFactory<DetectionType, String> getControllerFactory() {
    return DetectionTypeControllerFactory::createController;
  }
}

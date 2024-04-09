package edu.colorado.cires.pace.cli.command.platform;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.Platform;
import picocli.CommandLine.Command;

@Command(name = "list", description = "List all platforms", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class FindAllPlatformsCommand extends FindAllCommand<Platform> {

  @Override
  protected ControllerFactory<Platform> getControllerFactory() {
    return PlatformControllerFactory::createController;
  }
}

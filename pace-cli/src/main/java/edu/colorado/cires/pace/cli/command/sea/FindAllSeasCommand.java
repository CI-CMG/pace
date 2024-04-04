package edu.colorado.cires.pace.cli.command.sea;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.Sea;
import picocli.CommandLine.Command;

@Command(name = "list", description = "List all sea areas", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class FindAllSeasCommand extends FindAllCommand<Sea, String> {

  @Override
  protected ControllerFactory<Sea, String> getControllerFactory() {
    return SeaControllerFactory::createController;
  }
}

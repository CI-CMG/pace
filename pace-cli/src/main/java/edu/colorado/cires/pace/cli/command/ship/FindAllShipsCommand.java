package edu.colorado.cires.pace.cli.command.ship;

import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.data.Ship;
import picocli.CommandLine.Command;

@Command(name = "list", description = "List all ships", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class FindAllShipsCommand extends FindAllCommand<Ship, String> {


  @Override
  protected ControllerFactory<Ship, String> getControllerFactory() {
    return ShipControllerFactory::createController;
  }
}

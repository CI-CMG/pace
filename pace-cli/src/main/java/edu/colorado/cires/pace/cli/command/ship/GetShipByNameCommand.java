package edu.colorado.cires.pace.cli.command.ship;

import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.GetByUniqueFieldCommand;
import edu.colorado.cires.pace.data.Ship;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get-by-name", description = "Get ship by name", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class GetShipByNameCommand extends GetByUniqueFieldCommand<Ship> {
  
  @Parameters(description = "ship name")
  private String name;

  @Override
  protected ControllerFactory<Ship> getControllerFactory() {
    return ShipControllerFactory::createController;
  }

  @Override
  protected Supplier<String> getUniqueFieldProvider() {
    return () -> name;
  }
}

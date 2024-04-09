package edu.colorado.cires.pace.cli.command.ship;

import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.DeleteCommand;
import edu.colorado.cires.pace.data.Ship;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "delete", description = "Delete ship", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class DeleteShipCommand extends DeleteCommand<Ship> {
  
  @Parameters(description = "ship uuid")
  private UUID uuid;

  @Override
  protected ControllerFactory<Ship> getControllerFactory() {
    return ShipControllerFactory::createController;
  }

  @Override
  protected Supplier<UUID> getUUIDProvider() {
    return () -> uuid;
  }
}

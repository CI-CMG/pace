package edu.colorado.cires.pace.cli.command.ship;

import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.GetByUUIDCommand;
import edu.colorado.cires.pace.data.Ship;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get-by-uuid", description = "Get ship by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class GetShipByUUIDCommand extends GetByUUIDCommand<Ship> {
  
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

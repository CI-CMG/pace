package edu.colorado.cires.pace.cli.command.ship;

import edu.colorado.cires.pace.cli.command.base.VersionProvider;
import edu.colorado.cires.pace.cli.command.common.GetByUUIDCommand;
import edu.colorado.cires.pace.core.controller.CRUDController;
import edu.colorado.cires.pace.data.Ship;
import java.io.IOException;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get-by-uuid", description = "Get ship by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class GetShipByUUIDCommand extends GetByUUIDCommand<Ship, String> {
  
  @Parameters(description = "ship uuid")
  private UUID uuid;

  @Override
  protected CRUDController<Ship, String> createController() throws IOException {
    return ShipControllerFactory.createController(
        getDatastoreDirectory(),
        objectMapper
    );
  }

  @Override
  protected Supplier<UUID> getUUIDProvider() {
    return () -> uuid;
  }
}

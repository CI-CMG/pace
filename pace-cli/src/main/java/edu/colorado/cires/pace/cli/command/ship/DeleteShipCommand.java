package edu.colorado.cires.pace.cli.command.ship;

import edu.colorado.cires.pace.cli.command.base.VersionProvider;
import edu.colorado.cires.pace.cli.command.common.DeleteCommand;
import edu.colorado.cires.pace.core.controller.CRUDController;
import edu.colorado.cires.pace.data.Ship;
import java.io.IOException;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "delete", description = "Delete ship", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class DeleteShipCommand extends DeleteCommand<Ship, String> {
  
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

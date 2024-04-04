package edu.colorado.cires.pace.cli.command.ship;

import edu.colorado.cires.pace.cli.command.base.VersionProvider;
import edu.colorado.cires.pace.cli.command.common.CreateCommand;
import edu.colorado.cires.pace.core.controller.CRUDController;
import edu.colorado.cires.pace.data.Ship;
import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "create", description = "Create ship", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class CreateShipCommand extends CreateCommand<Ship, String> {
  
  @Parameters(description = "Ship from file (- for stdin)")
  private File ship;

  @Override
  protected CRUDController<Ship, String> createController() throws IOException {
    return ShipControllerFactory.createController(
        getDatastoreDirectory(),
        objectMapper
    );
  }

  @Override
  protected Supplier<File> getJsonBlobProvider() {
    return () -> this.ship;
  }

  @Override
  protected Class<Ship> getJsonClass() {
    return Ship.class;
  }
}

package edu.colorado.cires.pace.cli.command.ship;

import edu.colorado.cires.pace.cli.command.base.VersionProvider;
import edu.colorado.cires.pace.cli.command.common.GetByUniqueFieldCommand;
import edu.colorado.cires.pace.core.controller.CRUDController;
import edu.colorado.cires.pace.data.Ship;
import java.io.IOException;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get-by-name", description = "Get ship by name", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class GetShipByNameCommand extends GetByUniqueFieldCommand<Ship, String> {
  
  @Parameters(description = "ship name")
  private String name;

  @Override
  protected CRUDController<Ship, String> createController() throws IOException {
    return ShipControllerFactory.createController(
        getDatastoreDirectory(),
        objectMapper
    );
  }

  @Override
  protected Supplier<String> getUniqueFieldProvider() {
    return () -> name;
  }
}

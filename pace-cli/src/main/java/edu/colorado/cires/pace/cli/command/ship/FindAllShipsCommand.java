package edu.colorado.cires.pace.cli.command.ship;

import edu.colorado.cires.pace.cli.command.base.VersionProvider;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.core.controller.CRUDController;
import edu.colorado.cires.pace.data.Ship;
import java.io.IOException;
import picocli.CommandLine.Command;

@Command(name = "list", description = "List all ships", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class FindAllShipsCommand extends FindAllCommand<Ship, String> {

  @Override
  protected CRUDController<Ship, String> createController() throws IOException {
    return ShipControllerFactory.createController(
        getDatastoreDirectory(),
        objectMapper
    );
  }
}

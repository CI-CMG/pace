package edu.colorado.cires.pace.cli.command.ship;

import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import picocli.CommandLine.Command;

@Command(
    name = "ship",
    description = "Manage ships",
    mixinStandardHelpOptions = true,
    versionProvider = VersionProvider.class,
    subcommands = { 
        CreateShipCommand.class,
        GetShipByUUIDCommand.class,
        GetShipByNameCommand.class,
        FindAllShipsCommand.class,
        UpdateShipCommand.class,
        DeleteShipCommand.class
    }
)
public class ShipCommand implements Runnable {

  @Override
  public void run() {}
}

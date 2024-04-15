package edu.colorado.cires.pace.cli.command.ship;

import edu.colorado.cires.pace.cli.command.common.BaseCommand;
import edu.colorado.cires.pace.data.object.Ship;
import picocli.CommandLine.Command;

@Command(name = "ship", description = "Manage ships")
public class ShipCommand extends BaseCommand<Ship> {

  ShipCommand() {
    super(Ship.class, ShipRepositoryFactory::createRepository);
  }

  @Override
  public void run() {
    
  }
}

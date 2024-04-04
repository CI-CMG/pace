package edu.colorado.cires.pace.cli.command.base;

import edu.colorado.cires.pace.cli.command.ship.ShipCommand;
import picocli.CommandLine.Command;

@Command(
    name = "pace-cli",
    mixinStandardHelpOptions = true,
    description = "Passive Acoustic Collection Engine",
    versionProvider = VersionProvider.class,
    subcommands = {ShipCommand.class}
)
public class PaceCLI implements Runnable {

  @Override
  public void run() {}
}

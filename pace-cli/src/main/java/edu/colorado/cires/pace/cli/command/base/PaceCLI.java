package edu.colorado.cires.pace.cli.command.base;

import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.cli.command.platform.PlatformCommand;
import edu.colorado.cires.pace.cli.command.project.ProjectCommand;
import edu.colorado.cires.pace.cli.command.sea.SeaCommand;
import edu.colorado.cires.pace.cli.command.ship.ShipCommand;
import picocli.CommandLine.Command;

@Command(
    name = "pace-cli",
    mixinStandardHelpOptions = true,
    description = "Passive Acoustic Collection Engine",
    versionProvider = VersionProvider.class,
    subcommands = { ShipCommand.class, SeaCommand.class, ProjectCommand.class, PlatformCommand.class }
)
public class PaceCLI implements Runnable {

  @Override
  public void run() {}

}

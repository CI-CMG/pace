package edu.colorado.cires.pace.cli.command.sea;

import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import picocli.CommandLine.Command;

@Command(
    name = "sea-area",
    description = "Manage sea areas",
    mixinStandardHelpOptions = true,
    versionProvider = VersionProvider.class,
    subcommands = { 
        CreateSeaCommand.class,
        DeleteSeaCommand.class,
        FindAllSeasCommand.class,
        GetSeaByNameCommand.class,
        GetSeaByUUIDCommand.class,
        UpdateSeaCommand.class
    }
)
public class SeaCommand implements Runnable {

  @Override
  public void run() {}
}

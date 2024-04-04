package edu.colorado.cires.pace.cli.command.platform;

import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import picocli.CommandLine.Command;

@Command(
    name = "platform",
    description = "Manage platforms",
    mixinStandardHelpOptions = true,
    versionProvider = VersionProvider.class,
    subcommands = {
        CreatePlatformCommand.class,
        DeletePlatformCommand.class,
        FindAllPlatformsCommand.class,
        GetPlatformByNameCommand.class,
        GetPlatformByUUIDCommand.class,
        UpdatePlatformCommand.class
    }
)
public class PlatformCommand implements Runnable {

  @Override
  public void run() {}
}

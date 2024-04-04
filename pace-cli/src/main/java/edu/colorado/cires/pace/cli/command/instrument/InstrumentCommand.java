package edu.colorado.cires.pace.cli.command.instrument;

import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import picocli.CommandLine.Command;

@Command(
    name = "instrument",
    description = "Manage instruments",
    mixinStandardHelpOptions = true,
    versionProvider = VersionProvider.class,
    subcommands = {
        CreateInstrumentCommand.class,
        DeleteInstrumentCommand.class,
        FindAllInstrumentsCommand.class,
        GetInstrumentByNameCommand.class,
        GetInstrumentByUUIDCommand.class,
        UpdateInstrumentCommand.class
    }
)
public class InstrumentCommand implements Runnable {

  @Override
  public void run() {
    
  }
}

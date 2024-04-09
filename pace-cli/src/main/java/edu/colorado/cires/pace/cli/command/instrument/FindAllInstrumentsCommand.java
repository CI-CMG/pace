package edu.colorado.cires.pace.cli.command.instrument;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.Instrument;
import picocli.CommandLine.Command;

@Command(name = "list", description = "List all instruments", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class FindAllInstrumentsCommand extends FindAllCommand<Instrument> {

  @Override
  protected ControllerFactory<Instrument> getControllerFactory() {
    return InstrumentControllerFactory::createController;
  }
}

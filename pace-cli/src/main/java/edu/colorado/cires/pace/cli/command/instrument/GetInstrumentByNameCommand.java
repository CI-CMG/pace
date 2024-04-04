package edu.colorado.cires.pace.cli.command.instrument;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.GetByUniqueFieldCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.Instrument;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get-by-name", description = "Get instrument by name", versionProvider = VersionProvider.class)
class GetInstrumentByNameCommand extends GetByUniqueFieldCommand<Instrument, String> {
  
  @Parameters(description = "instrument name")
  private String name;

  @Override
  protected Supplier<String> getUniqueFieldProvider() {
    return () -> name;
  }

  @Override
  protected ControllerFactory<Instrument, String> getControllerFactory() {
    return InstrumentControllerFactory::createController;
  }
}

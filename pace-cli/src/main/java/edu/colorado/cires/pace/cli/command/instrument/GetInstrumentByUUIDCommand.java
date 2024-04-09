package edu.colorado.cires.pace.cli.command.instrument;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.GetByUUIDCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.Instrument;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get-by-uuid", description = "Get instrument by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class GetInstrumentByUUIDCommand extends GetByUUIDCommand<Instrument> {
  
  @Parameters(description = "instrument uuid")
  private UUID uuid;

  @Override
  protected Supplier<UUID> getUUIDProvider() {
    return () -> uuid;
  }

  @Override
  protected ControllerFactory<Instrument> getControllerFactory() {
    return InstrumentControllerFactory::createController;
  }
}

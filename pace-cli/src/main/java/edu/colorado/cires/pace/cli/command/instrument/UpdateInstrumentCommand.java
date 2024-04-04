package edu.colorado.cires.pace.cli.command.instrument;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.UpdateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.core.repository.UUIDProvider;
import edu.colorado.cires.pace.data.Instrument;
import java.io.File;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "update", description = "Update instrument", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class UpdateInstrumentCommand extends UpdateCommand<Instrument, String> {
  
  @Parameters(description = "File containing instrument (- for stdin)")
  private File instrument;

  @Override
  protected UUIDProvider<Instrument> getUUIDProvider() {
    return Instrument::getUUID;
  }

  @Override
  protected Supplier<File> getJsonBlobProvider() {
    return () -> instrument;
  }

  @Override
  protected Class<Instrument> getJsonClass() {
    return Instrument.class;
  }

  @Override
  protected ControllerFactory<Instrument, String> getControllerFactory() {
    return InstrumentControllerFactory::createController;
  }
}

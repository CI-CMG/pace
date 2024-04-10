package edu.colorado.cires.pace.cli.command.instrument;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.CreateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.Instrument;
import java.io.File;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "create", description = "Create instrument", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class CreateInstrumentCommand extends CreateCommand<Instrument> {
  
  @Parameters(description = "File containing instrument (- for stdin)")
  private File instrument;

  @Override
  protected Supplier<File> getJsonBlobProvider() {
    return () -> instrument;
  }

  @Override
  protected Class<Instrument> getJsonClass() {
    return Instrument.class;
  }

  @Override
  protected RepositoryFactory<Instrument> getRepositoryFactory() {
    return InstrumentRepositoryFactory::createRepository;
  }
}

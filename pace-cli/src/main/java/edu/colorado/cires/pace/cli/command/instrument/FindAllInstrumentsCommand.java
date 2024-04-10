package edu.colorado.cires.pace.cli.command.instrument;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.Instrument;
import picocli.CommandLine.Command;

@Command(name = "list", description = "List all instruments", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class FindAllInstrumentsCommand extends FindAllCommand<Instrument> {

  @Override
  protected RepositoryFactory<Instrument> getRepositoryFactory() {
    return InstrumentRepositoryFactory::createRepository;
  }
}

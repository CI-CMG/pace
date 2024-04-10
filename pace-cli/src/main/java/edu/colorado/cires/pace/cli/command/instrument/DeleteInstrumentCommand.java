package edu.colorado.cires.pace.cli.command.instrument;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.DeleteCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.Instrument;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "delete", description = "Delete instrument", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class DeleteInstrumentCommand extends DeleteCommand<Instrument> {
  
  @Parameters(description = "instrument uuid")
  private UUID uuid;

  @Override
  protected Supplier<UUID> getUUIDProvider() {
    return () -> uuid;
  }

  @Override
  protected RepositoryFactory<Instrument> getRepositoryFactory() {
    return InstrumentRepositoryFactory::createRepository;
  }
}

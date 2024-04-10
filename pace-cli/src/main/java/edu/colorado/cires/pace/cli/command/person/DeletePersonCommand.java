package edu.colorado.cires.pace.cli.command.person;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.DeleteCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.Person;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "delete", description = "Delete person", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class DeletePersonCommand extends DeleteCommand<Person> {
  
  @Parameters(description = "person uui")
  private UUID uuid;

  @Override
  protected Supplier<UUID> getUUIDProvider() {
    return () -> uuid;
  }

  @Override
  protected RepositoryFactory<Person> getRepositoryFactory() {
    return PersonRepositoryFactory::createRepository;
  }
}

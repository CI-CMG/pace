package edu.colorado.cires.pace.cli.command.person;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.CreateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.Person;
import java.io.File;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "create", description = "Create person", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class CreatePersonCommand extends CreateCommand<Person> {
  
  @Parameters(description = "File containing person (- for stdin)")
  private File person;

  @Override
  protected Supplier<File> getJsonBlobProvider() {
    return () -> person;
  }

  @Override
  protected Class<Person> getJsonClass() {
    return Person.class;
  }

  @Override
  protected RepositoryFactory<Person> getRepositoryFactory() {
    return PersonRepositoryFactory::createRepository;
  }
}

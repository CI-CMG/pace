package edu.colorado.cires.pace.cli.command.person;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.Person;
import picocli.CommandLine.Command;

@Command(name = "list", description = "List all people", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class FindAllPeopleCommand extends FindAllCommand<Person> {

  @Override
  protected RepositoryFactory<Person> getRepositoryFactory() {
    return PersonRepositoryFactory::createRepository;
  }
}

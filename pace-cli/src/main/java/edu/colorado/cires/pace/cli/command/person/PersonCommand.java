package edu.colorado.cires.pace.cli.command.person;

import edu.colorado.cires.pace.cli.command.common.BaseCommand;
import edu.colorado.cires.pace.data.object.Person;
import picocli.CommandLine.Command;

@Command(name = "person", description = "Manage people")
public class PersonCommand extends BaseCommand<Person> {

  PersonCommand() {
    super(Person.class, PersonRepositoryFactory::createRepository);
  }

  @Override
  public void run() {
    
  }
}

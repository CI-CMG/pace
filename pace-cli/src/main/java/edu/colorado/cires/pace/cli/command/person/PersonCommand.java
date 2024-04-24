package edu.colorado.cires.pace.cli.command.person;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.common.BaseCommand;
import edu.colorado.cires.pace.data.object.Person;
import java.util.List;
import picocli.CommandLine.Command;

@Command(name = "person", description = "Manage people")
public class PersonCommand extends BaseCommand<Person> {

  PersonCommand() {
    super(Person.class, PersonRepositoryFactory::createRepository, new TypeReference<>() {});
  }

  @Override
  public void run() {
    
  }
}

package edu.colorado.cires.pace.cli.command.person;

import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import picocli.CommandLine.Command;

@Command(
    name = "person",
    description = "Manage people",
    mixinStandardHelpOptions = true,
    versionProvider = VersionProvider.class,
    subcommands = {
        CreatePersonCommand.class,
        DeletePersonCommand.class,
        FindAllPeopleCommand.class,
        GetPersonByNameCommand.class,
        GetPersonByUUIDCommand.class,
        UpdatePersonCommand.class
    }
)
public class PersonCommand implements Runnable {

  @Override
  public void run() {
    
  }
}

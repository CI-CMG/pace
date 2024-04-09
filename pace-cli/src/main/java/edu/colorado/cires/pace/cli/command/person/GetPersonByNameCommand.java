package edu.colorado.cires.pace.cli.command.person;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.GetByUniqueFieldCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.Person;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get-by-name", description = "Get person by name", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class GetPersonByNameCommand extends GetByUniqueFieldCommand<Person> {
  
  @Parameters(description = "person name")
  private String name;

  @Override
  protected Supplier<String> getUniqueFieldProvider() {
    return () -> name;
  }

  @Override
  protected ControllerFactory<Person> getControllerFactory() {
    return PersonControllerFactory::createController;
  }
}

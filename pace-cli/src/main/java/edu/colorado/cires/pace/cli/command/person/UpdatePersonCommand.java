package edu.colorado.cires.pace.cli.command.person;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.UpdateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.Person;
import java.io.File;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "update", description = "Update person", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class UpdatePersonCommand extends UpdateCommand<Person> {
  
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
  protected ControllerFactory<Person> getControllerFactory() {
    return PersonControllerFactory::createController;
  }
}

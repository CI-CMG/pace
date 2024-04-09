package edu.colorado.cires.pace.cli.command.person;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.GetByUUIDCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.Person;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get-by-uuid", description = "Get person by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class GetPersonByUUIDCommand extends GetByUUIDCommand<Person> {
  
  @Parameters(description = "person uuid")
  private UUID uuid;

  @Override
  protected Supplier<UUID> getUUIDProvider() {
    return () -> uuid;
  }

  @Override
  protected ControllerFactory<Person> getControllerFactory() {
    return PersonControllerFactory::createController;
  }
}

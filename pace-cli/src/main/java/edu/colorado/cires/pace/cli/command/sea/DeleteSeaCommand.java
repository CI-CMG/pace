package edu.colorado.cires.pace.cli.command.sea;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.DeleteCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.Sea;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "delete", description = "Delete sea area", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class DeleteSeaCommand extends DeleteCommand<Sea, String> {
  
  @Parameters(description = "sea are uuid")
  private UUID uuid;

  @Override
  protected Supplier<UUID> getUUIDProvider() {
    return () -> uuid;
  }

  @Override
  protected ControllerFactory<Sea, String> getControllerFactory() {
    return SeaControllerFactory::createController;
  }
}

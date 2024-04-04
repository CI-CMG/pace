package edu.colorado.cires.pace.cli.command.sea;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.GetByUUIDCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.Sea;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get-by-uuid", description = "Get sea area by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class GetSeaByUUIDCommand extends GetByUUIDCommand<Sea, String> {
  
  @Parameters(description = "sea area uuid")
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

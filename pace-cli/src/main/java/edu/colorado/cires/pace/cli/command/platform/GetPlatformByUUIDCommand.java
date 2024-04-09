package edu.colorado.cires.pace.cli.command.platform;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.GetByUUIDCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.Platform;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get-by-uuid", description = "Get platform by uuid", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class GetPlatformByUUIDCommand extends GetByUUIDCommand<Platform> {
  
  @Parameters(description = "platform uuid")
  private UUID uuid;

  @Override
  protected Supplier<UUID> getUUIDProvider() {
    return () -> uuid;
  }

  @Override
  protected ControllerFactory<Platform> getControllerFactory() {
    return PlatformControllerFactory::createController;
  }
}

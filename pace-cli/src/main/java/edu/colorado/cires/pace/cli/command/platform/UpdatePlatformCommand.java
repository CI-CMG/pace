package edu.colorado.cires.pace.cli.command.platform;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.UpdateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.core.repository.UUIDProvider;
import edu.colorado.cires.pace.data.Platform;
import java.io.File;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "update", description = "Update platform", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class UpdatePlatformCommand extends UpdateCommand<Platform, String> {
  
  @Parameters(description = "File containing platform (- for stdin)")
  private File platform;

  @Override
  protected UUIDProvider<Platform> getUUIDProvider() {
    return Platform::getUUID;
  }

  @Override
  protected Supplier<File> getJsonBlobProvider() {
    return () -> platform;
  }

  @Override
  protected Class<Platform> getJsonClass() {
    return Platform.class;
  }

  @Override
  protected ControllerFactory<Platform, String> getControllerFactory() {
    return PlatformControllerFactory::createController;
  }
}

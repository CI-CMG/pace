package edu.colorado.cires.pace.cli.command.platform;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.UpdateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.Platform;
import java.io.File;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "update", description = "Update platform", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class UpdatePlatformCommand extends UpdateCommand<Platform> {
  
  @Parameters(description = "File containing platform (- for stdin)")
  private File platform;

  @Override
  protected Supplier<File> getJsonBlobProvider() {
    return () -> platform;
  }

  @Override
  protected Class<Platform> getJsonClass() {
    return Platform.class;
  }

  @Override
  protected RepositoryFactory<Platform> getRepositoryFactory() {
    return PlatformRepositoryFactory::createRepository;
  }
}

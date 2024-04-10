package edu.colorado.cires.pace.cli.command.platform;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.CreateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.Platform;
import java.io.File;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "create", description = "Create platform", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class CreatePlatformCommand extends CreateCommand<Platform> {
  
  @Parameters(description = "File containing platform (- for stdin)")
  private File file;

  @Override
  protected Supplier<File> getJsonBlobProvider() {
    return () -> file;
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

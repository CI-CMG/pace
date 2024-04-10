package edu.colorado.cires.pace.cli.command.platform;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.Platform;
import picocli.CommandLine.Command;

@Command(name = "list", description = "List all platforms", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class FindAllPlatformsCommand extends FindAllCommand<Platform> {

  @Override
  protected RepositoryFactory<Platform> getRepositoryFactory() {
    return PlatformRepositoryFactory::createRepository;
  }
}

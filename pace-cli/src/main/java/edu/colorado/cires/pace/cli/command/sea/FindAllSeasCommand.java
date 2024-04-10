package edu.colorado.cires.pace.cli.command.sea;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.Sea;
import picocli.CommandLine.Command;

@Command(name = "list", description = "List all sea areas", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class FindAllSeasCommand extends FindAllCommand<Sea> {

  @Override
  protected RepositoryFactory<Sea> getRepositoryFactory() {
    return SeaRepositoryFactory::createRepository;
  }
}

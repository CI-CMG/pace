package edu.colorado.cires.pace.cli.command.project;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.GetByUniqueFieldCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.Project;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "get-by-name", description = "Get project by name", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class GetProjectByNameCommand extends GetByUniqueFieldCommand<Project> {
  
  @Parameters(description = "project name")
  private String name;

  @Override
  protected Supplier<String> getUniqueFieldProvider() {
    return () -> name;
  }

  @Override
  protected RepositoryFactory<Project> getRepositoryFactory() {
    return ProjectRepositoryFactory::createRepository;
  }
}

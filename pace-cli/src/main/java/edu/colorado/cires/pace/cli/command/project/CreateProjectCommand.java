package edu.colorado.cires.pace.cli.command.project;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.CreateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.Project;
import java.io.File;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "create", description = "Create project", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class CreateProjectCommand extends CreateCommand<Project> {
  
  @Parameters(description = "File containing project (- for stdin)")
  private File project;

  @Override
  protected Supplier<File> getJsonBlobProvider() {
    return () -> project;
  }

  @Override
  protected Class<Project> getJsonClass() {
    return Project.class;
  }

  @Override
  protected RepositoryFactory<Project> getRepositoryFactory() {
    return ProjectRepositoryFactory::createRepository;
  }
}

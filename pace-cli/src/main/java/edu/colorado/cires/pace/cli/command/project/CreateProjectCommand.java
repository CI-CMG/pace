package edu.colorado.cires.pace.cli.command.project;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.CreateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.Project;
import java.io.File;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "create", description = "Create project", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class CreateProjectCommand extends CreateCommand<Project, String> {
  
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
  protected ControllerFactory<Project, String> getControllerFactory() {
    return ProjectControllerFactory::createController;
  }
}

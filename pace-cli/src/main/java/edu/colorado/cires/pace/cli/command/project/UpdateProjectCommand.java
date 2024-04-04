package edu.colorado.cires.pace.cli.command.project;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.UpdateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.core.repository.UUIDProvider;
import edu.colorado.cires.pace.data.Project;
import java.io.File;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "update", description = "Update project", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class UpdateProjectCommand extends UpdateCommand<Project, String> {
  
  @Parameters(description = "File containing project (- for stdin)")
  private File project;

  @Override
  protected UUIDProvider<Project> getUUIDProvider() {
    return Project::getUUID;
  }

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

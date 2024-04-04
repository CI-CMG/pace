package edu.colorado.cires.pace.cli.command.project;

import edu.colorado.cires.pace.cli.command.common.ControllerFactory;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.Project;
import picocli.CommandLine.Command;

@Command(name = "list", description = "List all projects", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class FindAllProjectsCommand extends FindAllCommand<Project, String> {

  @Override
  protected ControllerFactory<Project, String> getControllerFactory() {
    return ProjectControllerFactory::createController;
  }

}

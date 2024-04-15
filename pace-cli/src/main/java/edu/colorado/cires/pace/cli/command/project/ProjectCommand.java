package edu.colorado.cires.pace.cli.command.project;

import edu.colorado.cires.pace.cli.command.common.BaseCommand;
import edu.colorado.cires.pace.data.object.Project;
import picocli.CommandLine.Command;

@Command(name = "project", description = "Manage projects")
public class ProjectCommand extends BaseCommand<Project> {

  ProjectCommand() {
    super(Project.class, ProjectRepositoryFactory::createRepository);
  }

  @Override
  public void run() {
    
  }
}

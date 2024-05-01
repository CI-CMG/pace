package edu.colorado.cires.pace.cli.command.project;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.common.BaseCommand;
import edu.colorado.cires.pace.data.object.Project;
import picocli.CommandLine.Command;

@Command(name = "project", description = "Manage projects")
public class ProjectCommand extends BaseCommand<Project> {

  ProjectCommand() {
    super(Project.class, ProjectRepositoryFactory::createJsonRepository, new TypeReference<>() {});
  }

  @Override
  public void run() {
    
  }
}

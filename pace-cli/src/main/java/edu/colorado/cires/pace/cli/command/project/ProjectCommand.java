package edu.colorado.cires.pace.cli.command.project;

import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import picocli.CommandLine.Command;

@Command(
    name = "project",
    description = "Manage projects",
    mixinStandardHelpOptions = true,
    versionProvider = VersionProvider.class,
    subcommands = {
        CreateProjectCommand.class,
        DeleteProjectCommand.class,
        FindAllProjectsCommand.class,
        GetProjectByNameCommand.class,
        GetProjectByUUIDCommand.class,
        UpdateProjectCommand.class
    }
)
public class ProjectCommand implements Runnable {

    @Override
    public void run() {
        
    }
}

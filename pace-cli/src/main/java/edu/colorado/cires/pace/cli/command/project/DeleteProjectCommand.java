package edu.colorado.cires.pace.cli.command.project;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.DeleteCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.Project;
import java.util.UUID;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "delete", description = "Delete project", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class DeleteProjectCommand extends DeleteCommand<Project> {
  
  @Parameters(description = "project uui")
  private UUID uuid;

  @Override
  protected Supplier<UUID> getUUIDProvider() {
    return () -> uuid;
  }

  @Override
  protected RepositoryFactory<Project> getRepositoryFactory() {
    return ProjectRepositoryFactory::createRepository;
  }
}

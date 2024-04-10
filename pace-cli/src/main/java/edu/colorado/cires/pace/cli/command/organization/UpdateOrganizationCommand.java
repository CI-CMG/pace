package edu.colorado.cires.pace.cli.command.organization;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.UpdateCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.Organization;
import java.io.File;
import java.util.function.Supplier;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "update", description = "Update organization", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class UpdateOrganizationCommand extends UpdateCommand<Organization> {
  
  @Parameters(description = "File containing organization (- for stdin)")
  private File organization;

  @Override
  protected Supplier<File> getJsonBlobProvider() {
    return () -> organization;
  }

  @Override
  protected Class<Organization> getJsonClass() {
    return Organization.class;
  }

  @Override
  protected RepositoryFactory<Organization> getRepositoryFactory() {
    return OrganizationRepositoryFactory::createRepository;
  }
}

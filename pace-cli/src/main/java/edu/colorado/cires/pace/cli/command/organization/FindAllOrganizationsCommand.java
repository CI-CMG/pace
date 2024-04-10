package edu.colorado.cires.pace.cli.command.organization;

import edu.colorado.cires.pace.cli.command.common.RepositoryFactory;
import edu.colorado.cires.pace.cli.command.common.FindAllCommand;
import edu.colorado.cires.pace.cli.command.common.VersionProvider;
import edu.colorado.cires.pace.data.object.Organization;
import picocli.CommandLine.Command;

@Command(name = "list", description = "List all organizations", mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
class FindAllOrganizationsCommand extends FindAllCommand<Organization> {

  @Override
  protected RepositoryFactory<Organization> getRepositoryFactory() {
    return OrganizationRepositoryFactory::createRepository;
  }

}

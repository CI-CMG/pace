package edu.colorado.cires.pace.cli.command.organization;

import edu.colorado.cires.pace.cli.command.common.BaseCommand;
import edu.colorado.cires.pace.data.object.Organization;
import picocli.CommandLine.Command;

@Command(name = "organization", description = "Manage organizations")
public class OrganizationCommand extends BaseCommand<Organization> {

  OrganizationCommand() {
    super(Organization.class, OrganizationRepositoryFactory::createRepository);
  }

  @Override
  public void run() {
    
  }
}

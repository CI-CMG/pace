package edu.colorado.cires.pace.cli.command.organization;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.common.BaseCommand;
import edu.colorado.cires.pace.data.object.Organization;
import java.util.List;
import picocli.CommandLine.Command;

@Command(name = "organization", description = "Manage organizations")
public class OrganizationCommand extends BaseCommand<Organization> {

  OrganizationCommand() {
    super(Organization.class, OrganizationRepositoryFactory::createRepository, new TypeReference<>() {});
  }

  @Override
  public void run() {
    
  }
}

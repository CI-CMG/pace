package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.datastore.Datastore;
import java.util.List;

public class OrganizationRepository extends PackageDependencyRepository<Organization> {


  public OrganizationRepository(Datastore<Organization> datastore, Datastore<Package> dependencyDatastore) {
    super(datastore, true, dependencyDatastore);
  }

  @Override
  protected boolean dependencyAppliesToObject(Package dependency, Organization object) {
    String name = object.getName();
    return dependency.getFunders().contains(name) || dependency.getSponsors().contains(name);
  }

  @Override
  protected Package applyObjectToDependentObjects(Organization original, Organization updated, Package dependency) {
    String originalName = original.getName();
    String newName = updated.getName();
    
    List<String> sponsors = replaceStringInList(dependency.getSponsors(), originalName, newName);
    List<String> funders = replaceStringInList(dependency.getFunders(), originalName, newName);

    return dependency.setSponsors(sponsors)
        .setFunders(funders);
  }

}

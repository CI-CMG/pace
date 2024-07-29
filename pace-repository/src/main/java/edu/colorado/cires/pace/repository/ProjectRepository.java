package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.project.Project;
import edu.colorado.cires.pace.datastore.Datastore;

public class ProjectRepository extends PackageDependencyRepository<Project> {

  public ProjectRepository(Datastore<Project> datastore, Datastore<Package> packageDatastore) {
    super(datastore, packageDatastore);
  }

  @Override
  protected boolean dependencyAppliesToObject(Package dependency, Project object) {
    return dependency.getProjects().contains(object.getName());
  }

  @Override
  protected Package applyObjectToDependentObjects(Project original, Project updated, Package dependency) {
    return (Package) dependency.setProjects(
        replaceStringInList(
            dependency.getProjects(), original.getName(), updated.getName()
        )
    );
  }
}

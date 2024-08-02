package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.platform.Platform;
import edu.colorado.cires.pace.datastore.Datastore;

public class PlatformRepository extends PackageDependencyRepository<Platform> {

  public PlatformRepository(Datastore<Platform> datastore, Datastore<Package> packageDatastore) {
    super(datastore, packageDatastore);
  }

  @Override
  protected boolean dependencyAppliesToObject(Package dependency, Platform object) {
    return dependency.getPlatformName().equals(object.getName());
  }

  @Override
  protected Package applyObjectToDependentObjects(Platform original, Platform updated, Package dependency) {
    return dependency.setPlatformName(
        replaceString(
            dependency.getPlatformName(), original.getName(), updated.getName()
        )
    );
  }
}

package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.platform.Platform;
import edu.colorado.cires.pace.datastore.Datastore;

/**
 * PlatformRepository extends PackageDependencyRepository and holds specifically
 * platform objects
 */
public class PlatformRepository extends PackageDependencyRepository<Platform> {

  /**
   * Creates a platform repository
   * @param datastore holds platform objects
   * @param packageDatastore holds package objects
   */
  public PlatformRepository(Datastore<Platform> datastore, Datastore<Package> packageDatastore) {
    super(datastore, packageDatastore);
  }

  @Override
  protected boolean dependencyAppliesToObject(Package dependency, Platform object) {
    return dependency.getPlatform().equals(object.getName());
  }

  @Override
  protected Package applyObjectToDependentObjects(Platform original, Platform updated, Package dependency) {
    return dependency.toBuilder().platform(
        replaceString(
            dependency.getPlatform(), original.getName(), updated.getName()
        )
    ).build();
  }
}

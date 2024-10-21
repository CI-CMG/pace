package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.datastore.Datastore;

/**
 * PackageDependencyRepository extends UpstreamDependencyRepository and holds
 * package dependencies
 * @param <O> object type
 */
abstract class PackageDependencyRepository<O extends ObjectWithUniqueField> extends UpstreamDependencyRepository<O, Package> {

  /**
   * Creates a package dependency repository
   * @param datastore holds package dependency objects
   * @param writableUUID indicates if the uuid is writable
   * @param packageDatastore holds package objects
   */
  public PackageDependencyRepository(Datastore<O> datastore, boolean writableUUID, Datastore<Package> packageDatastore) {
    super(datastore, writableUUID, packageDatastore);
  }

  /**
   * Creates a package dependency repository
   * @param datastore holds package dependency objects
   * @param packageDatastore holds package objects
   */
  public PackageDependencyRepository(Datastore<O> datastore, Datastore<Package> packageDatastore) {
    super(datastore, false, packageDatastore);
  }

  @Override
  protected String getDependentObjectUniqueFieldName() {
    return "packageId";
  }

  @Override
  protected Class<Package> getDependentObjectClass() {
    return Package.class;
  }
}

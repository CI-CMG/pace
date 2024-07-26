package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.datastore.Datastore;

abstract class PackageDependencyRepository<O extends ObjectWithUniqueField> extends UpstreamDependencyRepository<O, Package> {

  public PackageDependencyRepository(Datastore<O> datastore, boolean writableUUID, Datastore<Package> packageDatastore) {
    super(datastore, writableUUID, packageDatastore);
  }

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

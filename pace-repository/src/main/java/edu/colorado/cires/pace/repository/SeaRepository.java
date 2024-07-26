package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.sea.Sea;
import edu.colorado.cires.pace.datastore.Datastore;

public class SeaRepository extends PackageDependencyRepository<Sea> {

  public SeaRepository(Datastore<Sea> datastore, Datastore<Package> packageDatastore) {
    super(datastore, packageDatastore);
  }

  @Override
  protected boolean dependencyAppliesToObject(Package dependency, Sea object) {
    if (dependency.getLocationDetail() instanceof MarineLocation marineLocation) {
      return marineLocation.getSeaArea().equals(object.getName());
    }

    return false;
  }

  @Override
  protected Package applyObjectToDependentObjects(Sea original, Sea updated, Package dependency) {
    MarineLocation locationDetail = ((MarineLocation) dependency.getLocationDetail());

    locationDetail = locationDetail.setSeaArea(
        replaceString(
            locationDetail.getSeaArea(), original.getName(), updated.getName()
        )
    );

    return dependency.setLocationDetail(locationDetail);
  }
}

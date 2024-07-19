package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.LocationDetail;
import edu.colorado.cires.pace.data.object.MobileMarineLocation;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.datastore.Datastore;

public class ShipRepository extends PackageDependencyRepository<Ship> {

  public ShipRepository(Datastore<Ship> datastore, Datastore<Package> packageDatastore) {
    super(datastore, packageDatastore);
  }

  @Override
  protected boolean dependencyAppliesToObject(Package dependency, Ship object) {
    LocationDetail locationDetail = dependency.getLocationDetail();
    
    if (locationDetail instanceof MobileMarineLocation mobileMarineLocation) {
      return mobileMarineLocation.getVessel().equals(object.getName());
    }
    
    return false;
  }

  @Override
  protected Package applyObjectToDependentObjects(Ship original, Ship updated, Package dependency) {
    MobileMarineLocation locationDetail = ((MobileMarineLocation) dependency.getLocationDetail());
    
    locationDetail = locationDetail.setVessel(
        replaceString(locationDetail.getVessel(), original.getName(), updated.getName())
    );

    return dependency.setLocationDetail(locationDetail);
  }
}

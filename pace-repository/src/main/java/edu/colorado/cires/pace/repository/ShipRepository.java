package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.LocationDetail;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MobileMarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.ship.Ship;
import edu.colorado.cires.pace.datastore.Datastore;

/**
 * ShipRepository extends PackageDependencyRepository and holds specifically
 * ship objects
 */
public class ShipRepository extends PackageDependencyRepository<Ship> {

  /**
   * Creates a ship repository
   * @param datastore holds ship objects
   * @param packageDatastore holds package objects
   */
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

    return dependency.toBuilder().locationDetail(locationDetail).build();
  }
}

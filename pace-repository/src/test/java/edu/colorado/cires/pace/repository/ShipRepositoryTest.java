package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.dataset.detections.DetectionsPackage;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.LocationDetail;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MobileMarineLocation;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.ship.Ship;
import edu.colorado.cires.pace.data.object.dataset.soundLevelMetrics.SoundLevelMetricsPackage;
import java.util.UUID;

class ShipRepositoryTest extends PackageDependencyRepositoryTest<Ship> {

  @Override
  protected CRUDRepository<Ship> createRepository() {
    return new ShipRepository(createDatastore(), createDatastore(packages, Package.class, "packageId"));
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected Class<Ship> getObjectClass() {
    return Ship.class;
  }

  @Override
  protected Ship createNewObject(int suffix) {
    return Ship.builder()
        .name(String.format("name-%s", suffix))
        .build();
  }

  @Override
  protected Ship copyWithUpdatedUniqueField(Ship object, String uniqueField) {
    return Ship.builder()
        .uuid(object.getUuid())
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertObjectsEqual(Ship expected, Ship actual, boolean checkUUID) {
    assertEquals(expected.getName(), actual.getName());
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
  }

  @Override
  protected boolean objectInDependentObject(Ship updated, UUID dependentObjectUUID) {
    Package p = packages.get(dependentObjectUUID);

    LocationDetail locationDetail = p.getLocationDetail();
    
    if (locationDetail instanceof MobileMarineLocation mobileMarineLocation) {
      return mobileMarineLocation.getVessel().equals(updated.getName());
    }

    return false;
  }

  @Override
  protected Package createAndSaveDependentObject(Ship object) {
    Package p = ((DetectionsPackage) PackageRepositoryTest.createDetectionsDataset(1)).toBuilder()
        .uuid(UUID.randomUUID())
        .locationDetail(MobileMarineLocation.builder()
            .seaArea("seaArea")
            .vessel(object.getName())
            .locationDerivationDescription("description")
            .build())
        .build();
    
    packages.put(p.getUuid(), p);
    return packages.get(p.getUuid());
  }

  @Override
  protected Package createAndSaveIndependentDependentObject() {
    Package p = ((SoundLevelMetricsPackage) PackageRepositoryTest.createSoundLevelMetricsDataset(1)).toBuilder()
        .uuid(UUID.randomUUID())
        .build();
    
    packages.put(p.getUuid(), p);
    return packages.get(p.getUuid());
  }
}

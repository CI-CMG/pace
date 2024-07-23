package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.DetectionsPackage;
import edu.colorado.cires.pace.data.object.LocationDetail;
import edu.colorado.cires.pace.data.object.MarineInstrumentLocation;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.data.object.Sea;
import edu.colorado.cires.pace.data.object.SoundLevelMetricsPackage;
import edu.colorado.cires.pace.data.object.StationaryMarineLocation;
import java.util.UUID;

class SeaRepositoryTest extends PackageDependencyRepositoryTest<Sea> {

  @Override
  protected CRUDRepository<Sea> createRepository() {
    return new SeaRepository(createDatastore(), createDatastore(packages, Package.class, "packageId"));
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected Class<Sea> getObjectClass() {
    return Sea.class;
  }

  @Override
  protected Sea createNewObject(int suffix) {
    return Sea.builder()
        .name(String.format("name-%s", suffix))
        .build();
  }

  @Override
  protected Sea copyWithUpdatedUniqueField(Sea object, String uniqueField) {
    return Sea.builder()
        .uuid(object.getUuid())
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertObjectsEqual(Sea expected, Sea actual, boolean checkUUID) {
    assertEquals(expected.getName(), actual.getName());
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
  }

  @Override
  protected boolean objectInDependentObject(Sea updated, UUID dependentObjectUUID) {
    Package p = packages.get(dependentObjectUUID);
    LocationDetail locationDetail = p.getLocationDetail();
    if (locationDetail instanceof StationaryMarineLocation stationaryMarineLocation) {
      return stationaryMarineLocation.getSeaArea().equals(updated.getName());
    }
    
    return false;
  }

  @Override
  protected Package createAndSaveDependentObject(Sea object) {
    Package p = ((DetectionsPackage) PackageRepositoryTest.createDetectionsDataset(1)).toBuilder()
        .uuid(UUID.randomUUID())
        .locationDetail(StationaryMarineLocation.builder()
            .seaArea(object.getName())
            .deploymentLocation(MarineInstrumentLocation.builder()
                .latitude(1d)
                .longitude(2d)
                .seaFloorDepth(4f)
                .instrumentDepth(8f)
                .build())
            .recoveryLocation(MarineInstrumentLocation.builder()
                .latitude(1d)
                .longitude(2d)
                .seaFloorDepth(4f)
                .instrumentDepth(8f)
                .build())
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

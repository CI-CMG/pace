package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.AudioPackage;
import edu.colorado.cires.pace.data.object.DetectionsPackage;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.data.object.Platform;
import java.util.UUID;

class PlatformRepositoryTest extends PackageDependencyRepositoryTest<Platform> {

  @Override
  protected CRUDRepository<Platform> createRepository() {
    return new PlatformRepository(createDatastore(), createDatastore(packages, Package.class, "packageId"));
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected Class<Platform> getObjectClass() {
    return Platform.class;
  }

  @Override
  protected Platform createNewObject(int suffix) {
    return Platform.builder()
        .name(String.format("name-%s", suffix))
        .build();
  }

  @Override
  protected Platform copyWithUpdatedUniqueField(Platform object, String uniqueField) {
    return Platform.builder()
        .uuid(object.getUuid())
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertObjectsEqual(Platform expected, Platform actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
    assertEquals(expected.getName(), actual.getName());
  }

  @Override
  protected boolean objectInDependentObject(Platform updated, UUID dependentObjectUUID) {
    return packages.get(dependentObjectUUID).getPlatform().equals(updated.getName());
  }

  @Override
  protected Package createAndSaveDependentObject(Platform object) {
    Package p = ((DetectionsPackage) PackageRepositoryTest.createDetectionsDataset(1)).toBuilder()
        .uuid(UUID.randomUUID())
        .platform(object.getName())
        .build();
    packages.put(p.getUuid(), p);
    return packages.get(p.getUuid());
  }

  @Override
  protected Package createAndSaveIndependentDependentObject() {
    Package p = ((AudioPackage) PackageRepositoryTest.createAudioPackingJob(1)).toBuilder()
        .uuid(UUID.randomUUID())
        .platform("unrelated-platform")
        .build();
    
    packages.put(p.getUuid(), p);
    return packages.get(p.getUuid());
  }
}

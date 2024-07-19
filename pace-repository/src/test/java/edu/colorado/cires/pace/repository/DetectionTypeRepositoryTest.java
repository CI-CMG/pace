package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.AudioPackage;
import edu.colorado.cires.pace.data.object.DetectionType;
import edu.colorado.cires.pace.data.object.DetectionsPackage;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.repository.search.DetectionTypeSearchParameters;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import java.util.List;
import java.util.UUID;

class DetectionTypeRepositoryTest extends PackageDependencyRepositoryTest<DetectionType> {

  @Override
  protected CRUDRepository<DetectionType> createRepository() {
    return new DetectionTypeRepository(createDatastore(), createDatastore(packages, Package.class, "packageId"));
  }

  @Override
  protected SearchParameters<DetectionType> createSearchParameters(List<DetectionType> objects) {
    return DetectionTypeSearchParameters.builder()
        .sources(objects.stream().map(DetectionType::getSource).toList())
        .build();
  }

  @Override
  protected String getUniqueFieldName() {
    return "source";
  }

  @Override
  protected Class<DetectionType> getObjectClass() {
    return DetectionType.class;
  }

  @Override
  protected DetectionType createNewObject(int suffix) {
    return DetectionType.builder()
        .scienceName(String.format("science-name-%s", suffix))
        .source(String.format("source-%s", suffix))
        .build();
  }

  @Override
  protected DetectionType copyWithUpdatedUniqueField(DetectionType object, String uniqueField) {
    return object.toBuilder()
        .source(uniqueField)
        .build();
  }

  @Override
  protected void assertObjectsEqual(DetectionType expected, DetectionType actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
    assertEquals(expected.getScienceName(), actual.getScienceName());
    assertEquals(expected.getScienceName(), actual.getScienceName());
  }

  @Override
  protected boolean objectInDependentObject(DetectionType updated, UUID dependentObjectUUID) {
    Package p = packages.get(dependentObjectUUID);
    if (p instanceof DetectionsPackage detectionsPackage) {
      return detectionsPackage.getSoundSource().equals(updated.getSource());
    }
    return false;
  }

  @Override
  protected Package createAndSaveDependentObject(DetectionType object) {
    Package p = ((DetectionsPackage) PackageRepositoryTest.createDetectionsDataset(1)).toBuilder()
        .uuid(UUID.randomUUID())
        .soundSource(object.getSource())
        .build();
    
    packages.put(p.getUuid(), p);
    return packages.get(p.getUuid());
  }

  @Override
  protected Package createAndSaveIndependentDependentObject() {
    Package p = ((AudioPackage) PackageRepositoryTest.createAudioPackingJob(1)).toBuilder()
        .uuid(UUID.randomUUID())
        .build();

    packages.put(p.getUuid(), p);
    return packages.get(p.getUuid());
  }
}

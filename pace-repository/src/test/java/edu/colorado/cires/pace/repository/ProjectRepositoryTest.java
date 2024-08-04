package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;
import edu.colorado.cires.pace.data.object.dataset.detections.DetectionsPackage;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.object.project.Project;
import java.util.Collections;
import java.util.UUID;

class ProjectRepositoryTest extends PackageDependencyRepositoryTest<Project> {

  @Override
  protected CRUDRepository<Project> createRepository() {
    return new ProjectRepository(createDatastore(), createDatastore(packages, Package.class, "packageId"));
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected Class<Project> getObjectClass() {
    return Project.class;
  }

  @Override
  protected Project createNewObject(int suffix) {
    return Project.builder()
        .name(String.format("name-%s", suffix))
        .build();
  }

  @Override
  protected Project copyWithUpdatedUniqueField(Project object, String uniqueField) {
    return Project.builder()
        .uuid(object.getUuid())
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertObjectsEqual(Project expected, Project actual, boolean checkUUID) {
    assertEquals(expected.getName(), actual.getName());
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
  }

  @Override
  protected boolean objectInDependentObject(Project updated, UUID dependentObjectUUID) {
    Package p = packages.get(dependentObjectUUID);
    return p.getProjects().contains(updated.getName());
  }

  @Override
  protected Package createAndSaveDependentObject(Project object) {
    Package p = ((DetectionsPackage) PackageRepositoryTest.createDetectionsDataset(1)).toBuilder()
        .uuid(UUID.randomUUID())
        .projects(Collections.singletonList(object.getName()))
        .build();
    packages.put(p.getUuid(), p);
    return packages.get(p.getUuid());
  }

  @Override
  protected Package createAndSaveIndependentDependentObject() {
    Package p = ((AudioPackage) PackageRepositoryTest.createAudioPackingJob(1)).toBuilder()
        .uuid(UUID.randomUUID())
        .projects(Collections.singletonList("unrelated-project"))
        .build();
    
    packages.put(p.getUuid(), p);
    return packages.get(p.getUuid());
  }
}

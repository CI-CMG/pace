package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.colorado.cires.pace.data.object.AudioPackage;
import edu.colorado.cires.pace.data.object.DetectionsPackage;
import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.object.Instrument;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.datastore.Datastore;
import edu.colorado.cires.pace.repository.search.InstrumentSearchParameters;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class InstrumentRepositoryTest extends PackageDependencyRepositoryTest<Instrument> {
  
  private static final Datastore<FileType> fileTypeRepository = mock(Datastore.class);
  
  static {
    try {
      when(fileTypeRepository.getClassName()).thenReturn(FileType.class.getSimpleName());
      when(fileTypeRepository.getUniqueFieldName()).thenReturn("type");
      when(fileTypeRepository.findByUniqueField(any())).thenReturn(Optional.of(FileType.builder()
              .uuid(UUID.randomUUID())
              .type(UUID.randomUUID().toString())
              .comment("comment")
          .build()));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected CRUDRepository<Instrument> createRepository() {
    return new InstrumentRepository(createDatastore(), fileTypeRepository, createDatastore(packages, Package.class, "packageId"));
  }

  @Override
  protected SearchParameters<Instrument> createSearchParameters(List<Instrument> objects) {
    return InstrumentSearchParameters.builder()
        .names(objects.stream().map(Instrument::getName).toList())
        .build();
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected Class<Instrument> getObjectClass() {
    return Instrument.class;
  }

  @Override
  protected Instrument createNewObject(int suffix) {
    FileType fileType1 = FileType.builder()
        .uuid(UUID.randomUUID())
        .type(String.format("file-type-1-%s", suffix))
        .comment("comment")
        .build();
    
    FileType fileType2 = FileType.builder()
        .uuid(UUID.randomUUID())
        .type(String.format("file-type-2-%s", suffix))
        .comment("comment")
        .build();
    
    return Instrument.builder()
        .name(String.format("name-%s", suffix))
        .fileTypes(List.of(
            fileType1.getType(), fileType2.getType()
        )).build();
  }

  @Override
  protected Instrument copyWithUpdatedUniqueField(Instrument object, String uniqueField) {
    return Instrument.builder()
        .uuid(object.getUuid())
        .name(uniqueField)
        .fileTypes(object.getFileTypes())
        .build();
  }

  @Override
  protected void assertObjectsEqual(Instrument expected, Instrument actual, boolean checkUUID) {
    assertEquals(expected.getName(), actual.getName());
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
    assertEquals(expected.getFileTypes(), actual.getFileTypes());
  }
  
  @Test
  void testFileTypeDoesNotExist() throws Exception {
    Instrument instrument = createNewObject(1);
    when(fileTypeRepository.findByUniqueField(instrument.getFileTypes().get(0))).thenReturn(Optional.empty());
    when(fileTypeRepository.findByUniqueField(instrument.getFileTypes().get(1))).thenReturn(Optional.of(FileType.builder()
            .type(instrument.getFileTypes().get(1))
            .comment("comment")
        .build()));
    
    ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> repository.create(instrument));
    assertEquals("Instrument validation failed", exception.getMessage());
    
    assertEquals(1, exception.getConstraintViolations().size());
    ConstraintViolation<?> constraintViolation = exception.getConstraintViolations().iterator().next();
    assertEquals("FileType with type = file-type-1-1 does not exist", constraintViolation.getMessage());
    assertEquals("fileTypes[0]", constraintViolation.getPropertyPath().toString());
  }

  @Override
  protected boolean objectInDependentObject(Instrument updated, UUID dependentObjectUUID) {
    return packages.get(dependentObjectUUID).getInstrument().equals(updated.getName());
  }

  @Override
  protected Package createAndSaveDependentObject(Instrument object) {
    Package p = ((DetectionsPackage) PackageRepositoryTest.createDetectionsDataset(1)).toBuilder()
        .uuid(UUID.randomUUID())
        .instrument(object.getName())
        .build();
    packages.put(p.getUuid(), p);
    return packages.get(p.getUuid());
  }

  @Override
  protected Package createAndSaveIndependentDependentObject() {
    Package p = ((AudioPackage) PackageRepositoryTest.createAudioPackingJob(1)).toBuilder()
        .uuid(UUID.randomUUID())
        .instrument("unrelated-instrument")
        .build();
    
    packages.put(p.getUuid(), p);
    return packages.get(p.getUuid());
  }
}

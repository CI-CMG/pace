package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.object.Instrument;
import edu.colorado.cires.pace.repository.search.FileTypeSearchParameters;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

class FileTypeRepositoryTest extends UpstreamDependencyRepositoryTest<FileType, Instrument> {
  
  private final Map<UUID, Instrument> instruments = new HashMap<>(0);

  @BeforeEach
  void setUp() {
    instruments.clear();
  }

  @AfterEach
  void tearDown() {
    instruments.clear();
  }

  @Override
  protected CRUDRepository<FileType> createRepository() {
    return new FileTypeRepository(createDatastore(), createDatastore(instruments, Instrument.class, "name"));
  }

  @Override
  protected SearchParameters<FileType> createSearchParameters(List<FileType> objects) {
    return FileTypeSearchParameters.builder()
        .types(objects.stream().map(FileType::getType).toList())
        .build();
  }

  @Override
  protected String getUniqueFieldName() {
    return "type";
  }

  @Override
  protected Class<FileType> getObjectClass() {
    return FileType.class;
  }

  @Override
  protected FileType createNewObject(int suffix) {
    return FileType.builder()
        .type(String.format("type-%s", suffix))
        .comment(String.format("comment-%s", suffix))
        .build();
  }

  @Override
  protected FileType copyWithUpdatedUniqueField(FileType object, String uniqueField) {
    return FileType.builder()
        .uuid(object.getUuid())
        .type(uniqueField)
        .comment(object.getComment())
        .build();
  }

  @Override
  protected void assertObjectsEqual(FileType expected, FileType actual, boolean checkUUID) {
    assertEquals(expected.getComment(), actual.getComment());
    assertEquals(expected.getType(), actual.getType());
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
  }

  @Override
  protected Class<Instrument> getDependentObjectClass() {
    return Instrument.class;
  }

  @Override
  protected boolean objectInDependentObject(FileType updated, UUID dependentObjectUUID) {
    return instruments.get(dependentObjectUUID).getFileTypes()
        .stream()
        .anyMatch(type -> updated.getType().equals(type));
  }

  @Override
  protected Instrument createAndSaveDependentObject(FileType object) {
    UUID uuid = UUID.randomUUID();
    Instrument instrument = Instrument.builder()
        .uuid(uuid)
        .name("test-instrument")
        .fileTypes(Collections.singletonList(object.getType()))
        .build();
    instruments.put(uuid, instrument);
    
    return instruments.get(uuid);
  }
}

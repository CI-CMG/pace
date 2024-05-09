package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.ObjectWithUUID;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class JsonDatastoreTest<O extends ObjectWithUniqueField> {
  
  private static final Path TEST_PATH = Paths.get("target").resolve("test-dir");
  protected static final ObjectMapper OBJECT_MAPPER = SerializationUtils.createObjectMapper();
  protected abstract Class<O> getClazz();
  protected abstract JsonDatastore<O> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException;
  protected abstract String getExpectedUniqueFieldName();
  private JsonDatastore<O> datastore;
  protected abstract TypeReference<List<O>> getTypeReference();
  
  @BeforeEach
  void beforeEach() throws IOException {
    FileUtils.deleteQuietly(TEST_PATH.toFile());
    Files.createDirectories(TEST_PATH);
    datastore = createDatastore(TEST_PATH, OBJECT_MAPPER);
  }
  
  @Test
  void testGetClassName() {
    assertEquals(getClazz().getSimpleName(), datastore.getClassName());
  }
  
  @Test
  void testGetUniqueFieldName() {
    assertEquals(getExpectedUniqueFieldName(), datastore.getUniqueFieldName());
  }
  
  @Test
  void testSave() throws IOException, DatastoreException {
    O object = createNewObject(0);

    O result = datastore.save(object);
    assertObjectsEqual(object, result);
    assertSavedObjectEqualsObject(object, result);

    result = datastore.save(object);
    assertObjectsEqual(object, result);
    assertSavedObjectEqualsObject(object, result);
  }
  
  @Test
  void testSaveDatastoreFileExists() throws IOException, DatastoreException {
    O object = createNewObject(0);

    O result = datastore.save(object);
    assertObjectsEqual(object, result);
    assertSavedObjectEqualsObject(object, result);
    
    JsonDatastore<O> newDatastore = createDatastore(TEST_PATH, OBJECT_MAPPER);
    object = createNewObject(0);
    O saved = newDatastore.save(object);
    assertObjectsEqual(object, saved);
    assertSavedObjectEqualsObject(object, saved);
  }
  
  @Test
  void testSaveFailed() {
    O object = createNewObject(0);
    
    FileUtils.deleteQuietly(TEST_PATH.toFile());
    
    Exception exception = assertThrows(DatastoreException.class, () -> datastore.save(object));
    assertTrue(exception.getMessage().endsWith(String.format(
        "%s save failed", object.getUuid()
    )));
  }
  
  @Test
  void testDelete() throws IOException, DatastoreException {
    O object = createNewObject(0);
    datastore.save(object);
    datastore.delete(object);
    
    assertTrue(getFileForObject(object).isEmpty());
  }
  
  @Test
  public void testDeleteFailed() throws DatastoreException {
    O object = createNewObject(0);
    datastore.save(object);
    
    FileUtils.deleteQuietly(TEST_PATH.toFile());
    
    Exception exception = assertThrows(DatastoreException.class, () -> datastore.delete(object));
    assertTrue(exception.getMessage().endsWith(String.format(
        "%s delete failed", object.getUuid()
    )));
  }
  
  @Test
  void testFindByUUID() throws DatastoreException {
    O object = createNewObject(0);
    O result = datastore.save(object);
    
    Optional<O> maybeResult = datastore.findByUUID(result.getUuid());
    assertTrue(maybeResult.isPresent());
    assertObjectsEqual(maybeResult.get(), result);

    object = createNewObject(0);
    maybeResult = datastore.findByUUID(object.getUuid());
    assertTrue(maybeResult.isEmpty());
  }
  
  @Test
  void testFindByUniqueField() throws DatastoreException {
    O object = createNewObject(0);
    O result = datastore.save(object);
    
    Optional<O> maybeResult = datastore.findByUniqueField(datastore.getUniqueFieldGetter().apply(result));
    assertTrue(maybeResult.isPresent());
    assertObjectsEqual(maybeResult.get(), result);

    object = createNewObject(1);
    maybeResult = datastore.findByUniqueField(datastore.getUniqueFieldGetter().apply(object));
    assertTrue(maybeResult.isEmpty());
  }
  
  @Test
  void testFindAll() throws DatastoreException {
    O object1 = createNewObject(0);
    object1 = datastore.save(object1);
    O object2 = createNewObject(1);
    object2 = datastore.save(object2);
    
    List<O> results = datastore.findAll()
        .sorted((Comparator.comparing(ObjectWithUUID::getUuid)))
        .toList();
    List<O> expected = Stream.of(object1, object2).sorted(Comparator.comparing(ObjectWithUUID::getUuid))
        .toList();
    
    assertEquals(expected.size(), results.size());
    
    for (int i = 0; i < expected.size(); i++) {
      assertObjectsEqual(expected.get(i), results.get(i));
    }
  }
  
  protected abstract O createNewObject(int suffix);
  protected abstract void assertObjectsEqual(O expected, O actual);
  
  private void assertSavedObjectEqualsObject(O expected, O actual) throws IOException {
    try (Stream<Path> paths = Files.walk(TEST_PATH)) {
      paths.map(Path::toFile)
          .filter(File::isFile)
          .findFirst().ifPresentOrElse(
              (f) -> {
                try {
                  O actualObject = OBJECT_MAPPER.readValue(f, getTypeReference()).stream()
                      .filter(o -> o.getUuid().equals(actual.getUuid()))
                      .findFirst().orElseThrow();
                  assertObjectsEqual(
                      expected,
                      actualObject
                  );
                } catch (IOException e) {
                  throw new IllegalStateException(String.format(
                      "Failed to deserialize %s", f
                  ), e);
                }
              },
              () -> {
                throw new IllegalStateException("Failed to find file");
              }
          );
    }
  }
  
  private Optional<File> getFileForObject(O object) throws IOException {
    try (Stream<Path> paths = Files.walk(TEST_PATH)) {
      return paths.map(Path::toFile)
          .filter(File::isFile)
          .filter(f -> f.getName().equals(String.format(
              "%s.json", object.getUuid()
          ))).findFirst();
    }
  }

}

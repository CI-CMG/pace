package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class JsonDatastoreTest<O, U> {
  
  private static final Path TEST_PATH = Paths.get("target").resolve("test-dir");
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  protected abstract Class<O> getClazz();
  protected abstract JsonDatastore<O, U> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException;
  private final JsonDatastore<O, U> datastore;

  {
    try {
      datastore = createDatastore(TEST_PATH, OBJECT_MAPPER);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected abstract UUIDProvider<O> createUUIDProvider();
  protected abstract UniqueFieldProvider<O, U> createUniqueFieldProvider();
  private final UUIDProvider<O> uuidProvider = createUUIDProvider();
  protected final UniqueFieldProvider<O, U> uniqueFieldProvider = createUniqueFieldProvider();
  
  @BeforeEach
  void beforeEach() throws IOException {
    cleanTestDir();
    Files.createDirectories(TEST_PATH);
  }
  
  private static void cleanTestDir() {
    if (!TEST_PATH.toFile().exists()) {
      return;
    }
    try (Stream<Path> paths = Files.walk(TEST_PATH)) {
      paths
          .map(Path::toFile)
          .filter(File::isFile)
          .forEach(f -> {
            try {
              Files.delete(f.toPath());
            } catch (IOException e) {
              throw new IllegalStateException(String.format(
                  "Failed to delete %s", f
              ), e);
            }
          });
    } catch (IOException e) {
      throw new IllegalStateException("Failed to clean test directory", e);
    }
  }
  
  @Test
  void testSave() throws IOException {
    O object = createNewObject();
    
    O result = datastore.save(object);
    assertObjectsEqual(object, result);
    assertSavedObjectEqualsObject(object, result);
    assertTrue(getFileForObject(result).isPresent());
  }
  
  @Test
  void testDelete() throws IOException {
    O object = createNewObject();
    datastore.save(object);
    datastore.delete(object);
    
    assertTrue(getFileForObject(object).isEmpty());
  }
  
  @Test
  void testFindByUUID() throws IOException {
    O object = createNewObject();
    O result = datastore.save(object);
    
    Optional<O> maybeResult = datastore.findByUUID(uuidProvider.getUUID(result));
    assertTrue(maybeResult.isPresent());
    assertObjectsEqual(maybeResult.get(), result);
    
    object = createNewObject();
    maybeResult = datastore.findByUUID(uuidProvider.getUUID(object));
    assertTrue(maybeResult.isEmpty());
  }
  
  @Test
  void testFindByUniqueField() throws IOException {
    O object = createNewObject();
    O result = datastore.save(object);
    
    Optional<O> maybeResult = datastore.findByUniqueField(uniqueFieldProvider.getUniqueField(result));
    assertTrue(maybeResult.isPresent());
    assertObjectsEqual(maybeResult.get(), result);
    
    object = createNewObject();
    maybeResult = datastore.findByUniqueField(uniqueFieldProvider.getUniqueField(object));
    assertTrue(maybeResult.isEmpty());
  }
  
  @Test
  void testFindAll() throws IOException {
    O object1 = createNewObject();
    object1 = datastore.save(object1);
    O object2 = createNewObject();
    object2 = datastore.save(object2);
    
    List<O> results = datastore.findAll()
        .sorted(((o1, o2) -> uuidProvider.getUUID(o1).compareTo(uuidProvider.getUUID(o2))))
        .toList();
    List<O> expected = Stream.of(object1, object2).sorted(((o1, o2) -> uuidProvider.getUUID(o1).compareTo(uuidProvider.getUUID(o2))))
        .toList();
    
    assertEquals(expected.size(), results.size());
    
    for (int i = 0; i < expected.size(); i++) {
      assertObjectsEqual(expected.get(i), results.get(i));
    }
  }
  
  protected abstract O createNewObject();
  protected abstract void assertObjectsEqual(O expected, O actual);
  
  private void assertSavedObjectEqualsObject(O expected, O actual) throws IOException {
    try (Stream<Path> paths = Files.walk(TEST_PATH)) {
      paths.map(Path::toFile)
          .filter(File::isFile)
          .filter(f -> f.getName().contains(uuidProvider.getUUID(actual).toString()))
          .findFirst().ifPresentOrElse(
              (f) -> {
                try {
                  assertObjectsEqual(
                      expected,
                      OBJECT_MAPPER.readValue(f, getClazz())
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
              "%s.json", uuidProvider.getUUID(object)
          ))).findFirst();
    }
  }

}

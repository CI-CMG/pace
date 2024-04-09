package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.ObjectWithName;
import edu.colorado.cires.pace.data.ObjectWithUUID;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class JsonDatastoreTest {

  record TestObject(UUID uuid, String name) implements ObjectWithName {

    @Override
    public ObjectWithUUID copyWithNewUUID(UUID uuid) {
      return new TestObject(
          uuid,
          name
      );
    }
  }
  
  private static final Path TEST_PATH = Paths.get("target").resolve("test-dir");
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private final JsonDatastore<TestObject> datastore;

  {
    try {
      datastore = new JsonDatastore<>(TEST_PATH, OBJECT_MAPPER, TestObject.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
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
    TestObject object = createNewObject();
    
    TestObject result = datastore.save(object);
    assertObjectsEqual(object, result);
    assertSavedObjectEqualsObject(object, result);
    assertTrue(getFileForObject(result).isPresent());
  }
  
  @Test
  void testDelete() throws IOException {
    TestObject object = createNewObject();
    datastore.save(object);
    datastore.delete(object);
    
    assertTrue(getFileForObject(object).isEmpty());
  }
  
  @Test
  void testFindByUUID() throws IOException {
    TestObject object = createNewObject();
    TestObject result = datastore.save(object);
    
    Optional<TestObject> maybeResult = datastore.findByUUID(result.uuid());
    assertTrue(maybeResult.isPresent());
    assertObjectsEqual(maybeResult.get(), result);
    
    object = createNewObject();
    maybeResult = datastore.findByUUID(object.uuid());
    assertTrue(maybeResult.isEmpty());
  }
  
  @Test
  void testFindByUniqueField() throws IOException {
    TestObject object = createNewObject();
    TestObject result = datastore.save(object);
    
    Optional<TestObject> maybeResult = datastore.findByUniqueField(result.uniqueField());
    assertTrue(maybeResult.isPresent());
    assertObjectsEqual(maybeResult.get(), result);
    
    object = createNewObject();
    maybeResult = datastore.findByUniqueField(object.uniqueField());
    assertTrue(maybeResult.isEmpty());
  }
  
  @Test
  void testFindAll() throws IOException {
    TestObject object1 = createNewObject();
    object1 = datastore.save(object1);
    TestObject object2 = createNewObject();
    object2 = datastore.save(object2);
    
    List<TestObject> results = datastore.findAll()
        .sorted((Comparator.comparing(ObjectWithUUID::uuid)))
        .toList();
    List<TestObject> expected = Stream.of(object1, object2).sorted(Comparator.comparing(ObjectWithUUID::uuid))
        .toList();
    
    assertEquals(expected.size(), results.size());
    
    for (int i = 0; i < expected.size(); i++) {
      assertObjectsEqual(expected.get(i), results.get(i));
    }
  }

  private TestObject createNewObject() {
    return new TestObject(
        UUID.randomUUID(),
        UUID.randomUUID().toString()
    );
  }

  private void assertObjectsEqual(TestObject expected, TestObject actual) {
    assertEquals(expected.uuid(), actual.uuid());
    assertEquals(expected.name(), actual.name());
  }
  
  private void assertSavedObjectEqualsObject(TestObject expected, TestObject actual) throws IOException {
    try (Stream<Path> paths = Files.walk(TEST_PATH)) {
      paths.map(Path::toFile)
          .filter(File::isFile)
          .filter(f -> f.getName().contains(actual.uuid().toString()))
          .findFirst().ifPresentOrElse(
              (f) -> {
                try {
                  assertObjectsEqual(
                      expected,
                      OBJECT_MAPPER.readValue(f, TestObject.class)
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
  
  private Optional<File> getFileForObject(TestObject object) throws IOException {
    try (Stream<Path> paths = Files.walk(TEST_PATH)) {
      return paths.map(Path::toFile)
          .filter(File::isFile)
          .filter(f -> f.getName().equals(String.format(
              "%s.json", object.uuid()
          ))).findFirst();
    }
  }

}

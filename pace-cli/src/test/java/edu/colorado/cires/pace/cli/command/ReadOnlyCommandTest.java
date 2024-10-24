package edu.colorado.cires.pace.cli.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.error.ExecutionErrorHandler.CLIError;
import edu.colorado.cires.pace.data.object.base.AbstractObject;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public abstract class ReadOnlyCommandTest<T extends AbstractObject> extends CLITest {
  
  
  public abstract T createObject(String uniqueField, boolean withUUID);
  protected abstract String getRepositoryDirectory();
  protected abstract String getCommandPrefix();
  protected abstract TypeReference<List<T>> getTypeReference();
  protected abstract Class<T> getClazz();
  protected abstract String getUniqueFieldName();
  protected abstract void assertObjectsEqual(T expected, T actual, boolean checkUUID) throws JsonProcessingException;
  protected abstract String getUniqueField(T object);
  protected abstract T updateObject(T original, String uniqueField);
  
  protected String getSearchParameterArgumentName() {
    return "--names";
  }
  
  public T createObject(String uniqueField) {
    return createObject(uniqueField, false);
  }
  
  @Test
  void testFindAll() throws IOException {
    T object1 = createObject("test-1");
    T object2 = createObject("test-2");
    
    object1 = writeObject(object1);
    object2 = writeObject(object2);

    clearOut();
    
    execute(getCommandPrefix(), "list");
    
    String output = getCommandOutput();
    
    List<T> results = objectMapper.readValue(output, getTypeReference());
    results = results.stream()
        .sorted((o1, o2) -> getUniqueField(o1).compareToIgnoreCase(
            getUniqueField(o2)
        )).toList();
    assertEquals(2, results.size());

    for (int i = 0; i < results.size(); i++) {
      T actual = results.get(i);
      T expected = i == 0 ? object1 : object2;
      assertObjectsEqual(expected, actual, true);
    }
  }

  @Test
  void testSearchByUniqueField() throws IOException {
    T object1 = createObject("test-1");
    T object2 = createObject("test-2");

    object1 = writeObject(object1);
    writeObject(object2);

    clearOut();

    execute(getCommandPrefix(), "list", getSearchParameterArgumentName(), getUniqueField(object1));

    String output = getCommandOutput();

    List<T> results = objectMapper.readValue(output, getTypeReference());
    results = results.stream()
        .sorted((o1, o2) -> getUniqueField(o1).compareToIgnoreCase(
            getUniqueField(o2)
        )).toList();
    assertEquals(1, results.size());

    assertObjectsEqual(object1, results.get(0), true);
  }

  @Test
  void testSearchByVisibility() throws IOException {
    T object1 = createObject("test-1");
    T object2 = (T) createObject("test-2").setVisible(false);

    object1 = writeObject(object1);
    object2 = writeObject(object2);

    clearOut();

    execute(getCommandPrefix(), "list", "--show-visible");

    String output = getCommandOutput();

    List<T> results = objectMapper.readValue(output, getTypeReference());
    results = results.stream()
        .sorted((o1, o2) -> getUniqueField(o1).compareToIgnoreCase(
            getUniqueField(o2)
        )).toList();
    assertEquals(1, results.size());

    assertObjectsEqual(object1, results.get(0), true);

    clearOut();

    execute(getCommandPrefix(), "list", "--show-hidden");

    output = getCommandOutput();

    results = objectMapper.readValue(output, getTypeReference());
    results = results.stream()
        .sorted((o1, o2) -> getUniqueField(o1).compareToIgnoreCase(
            getUniqueField(o2)
        )).toList();
    assertEquals(1, results.size());

    assertObjectsEqual(object2, results.get(0), true);

    clearOut();

    execute(getCommandPrefix(), "list", "--show-hidden", "--show-visible");

    output = getCommandOutput();

    results = objectMapper.readValue(output, getTypeReference());
    results = results.stream()
        .sorted((o1, o2) -> getUniqueField(o1).compareToIgnoreCase(
            getUniqueField(o2)
        )).toList();
    assertEquals(2, results.size());

    assertObjectsEqual(object1, results.get(0), true);
    assertObjectsEqual(object2, results.get(1), true);

    clearOut();

    execute(getCommandPrefix(), "list", getSearchParameterArgumentName(), getUniqueField(object1), "--show-visible", "--show-hidden");

    output = getCommandOutput();

    results = objectMapper.readValue(output, getTypeReference());
    results = results.stream()
        .sorted((o1, o2) -> getUniqueField(o1).compareToIgnoreCase(
            getUniqueField(o2)
        )).toList();
    assertEquals(1, results.size());

    assertObjectsEqual(object1, results.get(0), true);
  }

  @Test
  void testFindAllNoResults() throws JsonProcessingException {
    execute(getCommandPrefix(), "list");
    
    String output = getCommandOutput();
    
    List<?> list = objectMapper.readValue(output, List.class);
    assertTrue(list.isEmpty());
  }
  
  @Test
  void testGetByUniqueField() throws IOException {
    T object = createObject("test");
    
    T created = writeObject(object);
    
    clearOut();
    
    execute(getCommandPrefix(), String.format(
        "get-by-%s", getUniqueFieldCommandSuffix()
    ), getUniqueField(object));
    
    assertStdoutEqualsCreated(created);
  }
  
  @Test
  void testGetByUniqueFieldNotFound() throws JsonProcessingException {
    T object = createObject("test");
    
    execute(getCommandPrefix(), String.format(
        "get-by-%s", getUniqueFieldCommandSuffix()
    ), getUniqueField(object));
    
    CLIError cliError = getCLIException();
    assertNull(cliError.detail());
    assertEquals(String.format(
        "%s with %s = %s not found", getClazz().getSimpleName(), getUniqueFieldName(), getUniqueField(object)
    ), cliError.message());
  }

  protected String getUniqueFieldCommandSuffix() {
    return getUniqueFieldName();
  }

  @Test
  void testGetByUUID() throws IOException {
    T object = createObject("test");
    T created = writeObject(object);
    
    clearOut();
    
    execute(getCommandPrefix(), "get-by-uuid", created.getUuid().toString());
    
    assertStdoutEqualsCreated(created);
  }
  
  @Test
  void testGetByUUIDNotFound() throws JsonProcessingException {
    UUID uuid = UUID.randomUUID();
    
    execute(getCommandPrefix(), "get-by-uuid", uuid.toString());
    
    CLIError exception = getCLIException();
    assertNull(exception.detail());
    assertEquals(String.format(
        "%s with uuid = %s not found", getClazz().getSimpleName(), uuid
    ), exception.message());
  }
  
  public T writeObject(T object) throws IOException {
    File file = testPath.resolve("test.json").toFile();

    objectMapper.writeValue(file, object);
    
    execute(getCommandPrefix(), "create", file.toString());
    
    return getWrittenObject(object);
  }
  
  private T getWrittenObject(T object) throws IOException {
    return getWrittenObjects().stream()
        .filter(o -> getUniqueField(o).equals(getUniqueField(object)))
        .findFirst()
        .orElse(null);
  }
  
  private List<T> getWrittenObjects() throws IOException {
    return Files.walk(testPath.resolve("test-metadata").resolve(getRepositoryDirectory()))
        .map(Path::toFile)
        .filter(File::isFile)
        .map(file -> {
          try {
            return objectMapper.readValue(file, getClazz());
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }).toList();
  }

  private void assertStdoutEqualsCreated(T created) throws JsonProcessingException {
    String output = getCommandOutput();
    T object = objectMapper.readValue(output, getClazz());
    assertObjectsEqual(created, object, true);
  }

}

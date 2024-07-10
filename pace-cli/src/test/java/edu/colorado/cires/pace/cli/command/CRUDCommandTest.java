package edu.colorado.cires.pace.cli.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

public abstract class CRUDCommandTest<T extends ObjectWithUniqueField> extends CLITest {
  
  public abstract T createObject(String uniqueField);
  protected abstract String getRepositoryFileName();
  protected abstract String getCommandPrefix();
  protected abstract TypeReference<List<T>> getTypeReference();
  protected abstract Class<T> getClazz();
  protected abstract String getUniqueFieldName();
  protected abstract void assertObjectsEqual(T expected, T actual, boolean checkUUID) throws JsonProcessingException;
  protected abstract String getUniqueField(T object);
  protected abstract T updateObject(T original, String uniqueField);
  
  @Test
  void testCreateFromFile() throws IOException {
    T object = createObject("test");
    
    T created = writeObject(object);

    assertObjects(object, created);
  }
  
  @Test
  void testCreateFromStdin() throws IOException {
    T object = createObject("test");
    System.setIn(new ByteArrayInputStream(
        objectMapper.writeValueAsBytes(object)
    ));
    
    execute(getCommandPrefix(), "create", "-");
    
    assertObjects(object, getWrittenObject(object));
  }
  
  @Test
  void testFindAll() throws IOException {
    T object1 = createObject("test-1");
    T object2 = createObject("test-2");
    
    writeObject(object1);
    writeObject(object2);

    clearOut();
    
    execute(getCommandPrefix(), "list");
    
    String output = getCommandOutput();
    
    List<T> results = objectMapper.readValue(output, getTypeReference());
    assertEquals(2, results.size());

    for (int i = 0; i < results.size(); i++) {
      T actual = results.get(i);
      T expected = i == 0 ? object1 : object2;
      assertObjectsEqual(expected, actual, false);
    }
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
  void testUpdateFromFile() throws IOException {
    T object = createObject("test");
    T created = writeObject(object);
    clearOut();
    T toUpdate = updateObject(created, "test-new-name");

    File file = testPath.resolve("test.json").toFile();

    objectMapper.writeValue(file, toUpdate);
    
    execute(getCommandPrefix(), "update", file.toString());
    
    T updated = getWrittenObject(toUpdate);
    assertStdoutEqualsCreated(updated);
  }
  
  @Test
  void testUpdateFromStdin() throws IOException {
    T object = createObject("test");
    T created = writeObject(object);
    clearOut();
    T toUpdate = updateObject(created, "test-new-name");

    System.setIn(new ByteArrayInputStream(
        objectMapper.writeValueAsBytes(toUpdate)
    ));

    execute(getCommandPrefix(), "update", "-");

    T updated = getWrittenObject(toUpdate);
    assertStdoutEqualsCreated(updated);
  }
  
  @Test
  void testDelete() throws IOException {
    T object = createObject("test");
    T created = writeObject(object);
    
    execute(getCommandPrefix(), "delete", created.getUuid().toString());
    
    assertEquals(0, getWrittenObjects().size());
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
        .orElseThrow();
  }
  
  private List<T> getWrittenObjects() throws IOException {
    return objectMapper.readValue(
        testPath.resolve("test-metadata").resolve(getRepositoryFileName()).toFile(),
        getTypeReference()
    );
  }
  
  private void assertObjects(T original, T created) throws IOException {
    assertObjectsEqual(original, created, false);
    assertStdoutEqualsCreated(created);
//    assertLogMessage(created); // TODO
  }

  private void assertStdoutEqualsCreated(T created) throws JsonProcessingException {
    String output = getCommandOutput();
    T object = objectMapper.readValue(output, getClazz());
    assertObjectsEqual(created, object, true);
  }
  
  private void assertLogMessage(T created) {
    String logMessage = getCommandErr();
    assertTrue(logMessage.contains(String.format(
        "Created %s with %s = %s, uuid = %s",
        getClazz().getSimpleName(),
        getUniqueFieldName(),
        getUniqueField(created),
        created.getUuid()
    )));
  }

}

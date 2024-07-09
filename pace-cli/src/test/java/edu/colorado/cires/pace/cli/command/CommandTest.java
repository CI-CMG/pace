package edu.colorado.cires.pace.cli.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.base.PaceCLI;
import edu.colorado.cires.pace.cli.error.ExecutionErrorHandler;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

public abstract class CommandTest<T extends ObjectWithUniqueField> {
  
  public abstract T createObject(String uniqueField);
  protected abstract String getRepositoryFileName();
  protected abstract String getCommandPrefix();
  protected abstract TypeReference<List<T>> getTypeReference();
  protected abstract Class<T> getClazz();
  protected abstract String getUniqueFieldName();
  protected abstract void assertObjectsEqual(T expected, T actual, boolean checkUUID) throws JsonProcessingException;
  protected abstract String getUniqueField(T object);
  protected abstract T updateObject(T original, String uniqueField);

  private final CommandLine CLI = new CommandLine(new PaceCLI())
      .setExecutionExceptionHandler(new ExecutionErrorHandler());

  protected final ObjectMapper objectMapper = SerializationUtils.createObjectMapper();

  protected final Path testPath = Paths.get("target").resolve("test-dir");

  private final InputStream in = System.in;
  private final PrintStream out = System.out;
  private final PrintStream err = System.err;

  private final ByteArrayOutputStream commandOut = new ByteArrayOutputStream();
  private final ByteArrayOutputStream commandErr = new ByteArrayOutputStream();

  @BeforeEach
  public void beforeEach() throws IOException {
    FileUtils.deleteQuietly(testPath.toFile());
    FileUtils.forceMkdir(testPath.toFile());
    System.setOut(new PrintStream(commandOut));
    System.setErr(new PrintStream(commandErr));
  }

  @AfterEach
  public void afterEach() {
    FileUtils.deleteQuietly(testPath.toFile());
    System.setIn(in);
    System.setOut(out);
    System.setErr(err);
    commandOut.reset();
    commandErr.reset();
  }

  protected void execute(String... arguments) {
    CLI.execute(arguments);
  }

  protected String getCommandOutput() {
    return getStreamContent(commandOut);
  }

  protected String getCommandErr() {
    return getStreamContent(commandErr);
  }

  private String getStreamContent(ByteArrayOutputStream outputStream) {
    return outputStream.toString(StandardCharsets.UTF_8);
  }

  protected void clearOut() {
    commandOut.reset();
  }
  
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

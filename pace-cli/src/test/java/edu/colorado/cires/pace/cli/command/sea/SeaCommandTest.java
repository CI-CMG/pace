package edu.colorado.cires.pace.cli.command.sea;

import static edu.colorado.cires.pace.packaging.FileUtils.mkdir;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.ReadOnlyCommandTest;
import edu.colorado.cires.pace.cli.command.TranslateCommandTest;
import edu.colorado.cires.pace.data.object.sea.Sea;
import edu.colorado.cires.pace.data.object.sea.translator.SeaTranslator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public class SeaCommandTest extends ReadOnlyCommandTest<Sea> {

  @Override
  public Sea createObject(String uniqueField, boolean withUUID) {
    return Sea.builder()
        .uuid(withUUID ? UUID.randomUUID() : null)
        .name(uniqueField)
        .build();
  }

  @Override
  protected String getRepositoryDirectory() {
    return "seas";
  }

  @Override
  protected String getCommandPrefix() {
    return "sea";
  }

  @Override
  protected TypeReference<List<Sea>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected Class<Sea> getClazz() {
    return Sea.class;
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected void assertObjectsEqual(Sea expected, Sea actual, boolean checkUUID) {
    assertSeasEqual(expected, actual, checkUUID);
  }

  public static void assertSeasEqual(Sea expected, Sea actual, boolean checkUUID) {
    assertEquals(expected.getName(), actual.getName());
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    } else {
      assertNotNull(actual.getUuid());
    }
  }

  @Override
  protected String getUniqueField(Sea object) {
    return object.getName();
  }

  @Override
  protected Sea updateObject(Sea original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  public Sea writeObject(Sea object) throws IOException {
    return writeObject(testPath, object, objectMapper);
  }

  public static Sea writeObject(Path testPath, Sea object, ObjectMapper objectMapper) throws IOException {
    Path seasDirectory = testPath.resolve("test-metadata").resolve("seas").toAbsolutePath();
    mkdir(seasDirectory);
    object = object.toBuilder()
        .uuid(object.getUuid() == null ? UUID.randomUUID() : object.getUuid())
        .build();
    File file = seasDirectory.resolve(object.getUuid().toString()+".json").toFile();
    Files.createFile(file.toPath());

    objectMapper.writeValue(file, object);

    return objectMapper.readValue(file, Sea.class);
  }
}

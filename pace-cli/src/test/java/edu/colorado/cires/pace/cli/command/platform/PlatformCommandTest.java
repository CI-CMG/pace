package edu.colorado.cires.pace.cli.command.platform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.TranslateCommandTest;
import edu.colorado.cires.pace.data.object.platform.Platform;
import edu.colorado.cires.pace.data.object.platform.translator.PlatformTranslator;
import java.util.List;
import java.util.UUID;

public class PlatformCommandTest extends TranslateCommandTest<Platform, PlatformTranslator> {

  @Override
  public Platform createObject(String uniqueField, boolean withUUID) {
    return Platform.builder()
        .uuid(withUUID ? UUID.randomUUID() : null)
        .name(uniqueField)
        .build();
  }

  @Override
  protected String getRepositoryDirectory() {
    return "platforms";
  }

  @Override
  protected String getCommandPrefix() {
    return "platform";
  }

  @Override
  protected TypeReference<List<Platform>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected Class<Platform> getClazz() {
    return Platform.class;
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected void assertObjectsEqual(Platform expected, Platform actual, boolean checkUUID) {
    assertPlatformsEqual(expected, actual, checkUUID);
  }

  public static void assertPlatformsEqual(Platform expected, Platform actual, boolean checkUUID) {
    assertEquals(expected.getName(), actual.getName());
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    } else {
      assertNotNull(actual.getUuid());
    }
  }

  @Override
  protected String getUniqueField(Platform object) {
    return object.getName();
  }

  @Override
  protected Platform updateObject(Platform original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected String[] getTranslatorFields() {
    return new String[] {
      "UUID",
      "platformName"  
    };
  }

  @Override
  protected PlatformTranslator createTranslator(String name) {
    return PlatformTranslator.builder()
        .name(name)
        .platformUUID("UUID")
        .platformName("platformName")
        .build();
  }

  @Override
  protected String[] objectToRow(Platform object) {
    return new String[] {
        object.getUuid() == null ? "" : object.getUuid().toString(),
        object.getName()
    };
  }
}

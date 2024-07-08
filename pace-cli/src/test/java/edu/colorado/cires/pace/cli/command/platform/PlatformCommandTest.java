package edu.colorado.cires.pace.cli.command.platform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.TranslateCommandTest;
import edu.colorado.cires.pace.data.object.Platform;
import edu.colorado.cires.pace.data.translator.PlatformTranslator;
import java.util.List;

class PlatformCommandTest extends TranslateCommandTest<Platform, PlatformTranslator> {

  @Override
  public Platform createObject(String uniqueField) {
    return Platform.builder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected String getRepositoryFileName() {
    return "platforms.json";
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
      "platformUUID",
      "platformName"  
    };
  }

  @Override
  protected PlatformTranslator createTranslator(String name) {
    return PlatformTranslator.builder()
        .name(name)
        .platformUUID("platformUUID")
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

package edu.colorado.cires.pace.cli.command.sea;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.TranslateCommandTest;
import edu.colorado.cires.pace.data.object.Sea;
import edu.colorado.cires.pace.data.translator.SeaTranslator;
import java.util.List;
import java.util.UUID;

public class SeaCommandTest extends TranslateCommandTest<Sea, SeaTranslator> {

  @Override
  public Sea createObject(String uniqueField, boolean withUUID) {
    return Sea.builder()
        .uuid(withUUID ? UUID.randomUUID() : null)
        .name(uniqueField)
        .build();
  }

  @Override
  protected String getRepositoryFileName() {
    return "seas.json";
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
  protected String[] getTranslatorFields() {
    return new String[] {
        "UUID",
        "seaName"
    };
  }

  @Override
  protected SeaTranslator createTranslator(String name) {
    return SeaTranslator.builder()
        .name(name)
        .seaUUID("UUID")
        .seaName("seaName")
        .build();
  }

  @Override
  protected String[] objectToRow(Sea object) {
    return new String[] {
        object.getUuid() == null ? "" : object.getUuid().toString(),
        object.getName()
    };
  }
}

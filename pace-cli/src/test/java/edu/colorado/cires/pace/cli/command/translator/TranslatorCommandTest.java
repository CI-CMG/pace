package edu.colorado.cires.pace.cli.command.translator;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.CRUDCommandTest;
import edu.colorado.cires.pace.data.translator.Translator;
import java.util.List;

abstract class TranslatorCommandTest<T extends Translator> extends CRUDCommandTest<T> {

  @Override
  protected String getRepositoryFileName() {
    return "translators.json";
  }

  @Override
  protected String getCommandPrefix() {
    return "translator";
  }

  @Override
  protected TypeReference<List<T>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected Class<T> getClazz() {
    return (Class<T>) Translator.class;
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected void assertObjectsEqual(T expected, T actual, boolean checkUUID) {
    assertEquals(expected.getName(), actual.getName());
    
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    } else {
      assertNotNull(actual.getUuid());
    }
    
    assertTranslatorTypeSpecificFields(expected, actual);
  }

  @Override
  protected String getUniqueField(T object) {
    return object.getName();
  }
  
  protected abstract void assertTranslatorTypeSpecificFields(T expected, T actual);
}
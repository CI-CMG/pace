package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.translator.ShipTranslator;
import edu.colorado.cires.pace.data.translator.Translator;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public class TranslatorJsonDatastoreTest extends JsonDatastoreTest<Translator> {

  @Override
  protected Class<Translator> getClazz() {
    return Translator.class;
  }

  @Override
  protected TranslatorJsonDatastore createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new TranslatorJsonDatastore(storagePath, objectMapper);
  }

  @Override
  protected String getExpectedUniqueFieldName() {
    return "name";
  }

  @Override
  protected Translator createNewObject(int suffix) {
    return ShipTranslator.builder()
        .uuid(UUID.randomUUID())
        .name(UUID.randomUUID().toString())
        .shipUUID(UUID.randomUUID().toString())
        .shipName(UUID.randomUUID().toString())
        .build();
  }

  @Override
  protected void assertObjectsEqual(Translator expected, Translator actual) {
    assertEquals(expected.getUuid(), actual.getUuid());
    assertEquals(expected.getName(), actual.getName());
    ShipTranslator expectedShipTranslator = (ShipTranslator) expected;
    ShipTranslator actualShipTranslator = (ShipTranslator) actual;
    assertEquals(expectedShipTranslator.getShipUUID(), actualShipTranslator.getShipUUID());
    assertEquals(expectedShipTranslator.getShipName(), actualShipTranslator.getShipName());
  }
}

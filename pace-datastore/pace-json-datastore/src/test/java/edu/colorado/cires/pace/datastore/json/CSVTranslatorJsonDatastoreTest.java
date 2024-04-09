package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.CSVTranslator;
import edu.colorado.cires.pace.data.CSVTranslatorField;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

class CSVTranslatorJsonDatastoreTest extends JsonDatastoreTest<CSVTranslator> {

  @Override
  protected Class<CSVTranslator> getClazz() {
    return CSVTranslator.class;
  }

  @Override
  protected JsonDatastore<CSVTranslator> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new CSVTranslatorJsonDatastore(storagePath, objectMapper);
  }

  @Override
  protected CSVTranslator createNewObject() {
    CSVTranslatorField field1 = new CSVTranslatorField(
        "property1",
        1
    );
    CSVTranslatorField field2 = new CSVTranslatorField(
        "property2",
        2
    );

    return new CSVTranslator(
        UUID.randomUUID(),
        UUID.randomUUID().toString(),
        List.of(
            field1, field2
        )
    );
  }

  @Override
  protected void assertObjectsEqual(CSVTranslator expected, CSVTranslator actual) {
    assertEquals(expected.name(), actual.name());
    assertEquals(expected.uuid(), actual.uuid());

    List<CSVTranslatorField> expectedFields = expected.fields().stream()
        .sorted((f1, f2) -> f1.propertyName().compareToIgnoreCase(f2.propertyName()))
        .toList();
    List<CSVTranslatorField> actualFields = actual.fields().stream()
        .sorted((f1, f2) -> f1.propertyName().compareToIgnoreCase(f2.propertyName()))
        .toList();
    assertEquals(expectedFields.size(), actualFields.size());

    for (int i = 0; i < expected.fields().size(); i++) {
      assertEquals(expectedFields.get(i).columnNumber(), actualFields.get(i).columnNumber());
      assertEquals(expectedFields.get(i).propertyName(), actualFields.get(i).propertyName());
    }
  }
}

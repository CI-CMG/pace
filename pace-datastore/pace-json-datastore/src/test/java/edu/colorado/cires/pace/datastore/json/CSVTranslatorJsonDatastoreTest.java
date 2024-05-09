package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.data.object.CSVTranslatorField;
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
  protected String getExpectedUniqueFieldName() {
    return "name";
  }

  @Override
  protected TypeReference<List<CSVTranslator>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected CSVTranslator createNewObject(int suffix) {
    CSVTranslatorField field1 = CSVTranslatorField.builder()
        .propertyName("property1")
        .columnNumber(1)
        .build();
    CSVTranslatorField field2 = CSVTranslatorField.builder()
        .propertyName("property2")
        .columnNumber(2)
        .build();

    return CSVTranslator.builder()
        .uuid(UUID.randomUUID())
        .name(UUID.randomUUID().toString())
        .fields(List.of(
            field1, field2
        )).build();
  }

  @Override
  protected void assertObjectsEqual(CSVTranslator expected, CSVTranslator actual) {
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getUuid(), actual.getUuid());

    List<CSVTranslatorField> expectedFields = expected.getFields().stream()
        .sorted((f1, f2) -> f1.getPropertyName().compareToIgnoreCase(f2.getPropertyName()))
        .toList();
    List<CSVTranslatorField> actualFields = actual.getFields().stream()
        .sorted((f1, f2) -> f1.getPropertyName().compareToIgnoreCase(f2.getPropertyName()))
        .toList();
    assertEquals(expectedFields.size(), actualFields.size());

    for (int i = 0; i < expected.getFields().size(); i++) {
      assertEquals(expectedFields.get(i).getColumnNumber(), actualFields.get(i).getColumnNumber());
      assertEquals(expectedFields.get(i).getPropertyName(), actualFields.get(i).getPropertyName());
    }
  }
}

package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.ExcelTranslator;
import edu.colorado.cires.pace.data.ExcelTranslatorField;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

class ExcelTranslatorJsonDatastoreTest extends JsonDatastoreTest<ExcelTranslator> {

  @Override
  protected Class<ExcelTranslator> getClazz() {
    return ExcelTranslator.class;
  }

  @Override
  protected JsonDatastore<ExcelTranslator> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new ExcelTranslatorJsonDatastore(storagePath, objectMapper);
  }

  @Override
  protected ExcelTranslator createNewObject() {
    ExcelTranslatorField field1 = new ExcelTranslatorField(
        "property1",
        1,
        1
    );

    ExcelTranslatorField field2 = new ExcelTranslatorField(
        "property2",
        2,
        2
    );

    return new ExcelTranslator(
        UUID.randomUUID(),
        UUID.randomUUID().toString(),
        List.of(
            field1, field2
        )
    );
  }

  @Override
  protected void assertObjectsEqual(ExcelTranslator expected, ExcelTranslator actual) {
    assertEquals(expected.name(), actual.name());
    assertEquals(expected.uuid(), actual.uuid());

    List<ExcelTranslatorField> expectedFields = expected.fields().stream()
        .sorted((f1, f2) -> f1.propertyName().compareToIgnoreCase(f2.propertyName()))
        .toList();
    List<ExcelTranslatorField> actualFields = actual.fields().stream()
        .sorted((f1, f2) -> f1.propertyName().compareToIgnoreCase(f2.propertyName()))
        .toList();
    assertEquals(expectedFields.size(), actualFields.size());

    for (int i = 0; i < expected.fields().size(); i++) {
      assertEquals(expectedFields.get(i).columnNumber(), actualFields.get(i).columnNumber());
      assertEquals(expectedFields.get(i).propertyName(), actualFields.get(i).propertyName());
      assertEquals(expectedFields.get(i).sheetNumber(), actualFields.get(i).sheetNumber());
    }
  }
}

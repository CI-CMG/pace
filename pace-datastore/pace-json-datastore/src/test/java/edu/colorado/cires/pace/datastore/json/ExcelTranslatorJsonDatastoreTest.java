package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.object.ExcelTranslatorField;
import edu.colorado.cires.pace.data.object.ExcelTranslatorFieldImpl;
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
  protected String getExpectedUniqueFieldName() {
    return "name";
  }

  @Override
  protected ExcelTranslator createNewObject() {
    ExcelTranslatorFieldImpl field1 = ExcelTranslatorFieldImpl.builder()
        .propertyName("property1")
        .columnNumber(1)
        .sheetNumber(1)
        .build();

    ExcelTranslatorFieldImpl field2 = ExcelTranslatorFieldImpl.builder()
        .propertyName("property2")
        .columnNumber(2)
        .sheetNumber(2)
        .build();

    return ExcelTranslator.builder()
        .uuid(UUID.randomUUID())
        .name(UUID.randomUUID().toString())
        .fields(List.of(
            field1, field2
        )).build();
  }

  @Override
  protected void assertObjectsEqual(ExcelTranslator expected, ExcelTranslator actual) {
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getUuid(), actual.getUuid());

    List<ExcelTranslatorField> expectedFields = expected.getFields().stream()
        .sorted((f1, f2) -> f1.getPropertyName().compareToIgnoreCase(f2.getPropertyName()))
        .toList();
    List<ExcelTranslatorField> actualFields = actual.getFields().stream()
        .sorted((f1, f2) -> f1.getPropertyName().compareToIgnoreCase(f2.getPropertyName()))
        .toList();
    assertEquals(expectedFields.size(), actualFields.size());

    for (int i = 0; i < expected.getFields().size(); i++) {
      assertEquals(expectedFields.get(i).getColumnNumber(), actualFields.get(i).getColumnNumber());
      assertEquals(expectedFields.get(i).getPropertyName(), actualFields.get(i).getPropertyName());
      assertEquals(expectedFields.get(i).getSheetNumber(), actualFields.get(i).getSheetNumber());
    }
  }
}

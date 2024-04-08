package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.data.ExcelTranslator;
import edu.colorado.cires.pace.data.ExcelTranslatorField;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

class ExcelTranslatorJsonDatastoreTest extends JsonDatastoreTest<ExcelTranslator, String> {

  @Override
  protected Class<ExcelTranslator> getClazz() {
    return ExcelTranslator.class;
  }

  @Override
  protected JsonDatastore<ExcelTranslator, String> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new ExcelTranslatorJsonDatastore(storagePath, objectMapper);
  }

  @Override
  protected UUIDProvider<ExcelTranslator> createUUIDProvider() {
    return ExcelTranslator::getUUID;
  }

  @Override
  protected UniqueFieldProvider<ExcelTranslator, String> createUniqueFieldProvider() {
    return ExcelTranslator::getName;
  }

  @Override
  protected ExcelTranslator createNewObject() {
    ExcelTranslator translator = new ExcelTranslator();

    translator.setName(UUID.randomUUID().toString());
    translator.setUUID(UUID.randomUUID());

    ExcelTranslatorField field1 = new ExcelTranslatorField();
    field1.setColumnNumber(1);
    field1.setPropertyName("property1");
    field1.setSheetNumber(1);

    ExcelTranslatorField field2 = new ExcelTranslatorField();
    field2.setColumnNumber(2);
    field2.setPropertyName("property2");
    field2.setSheetNumber(2);

    translator.setFields(List.of(
        field1, field2
    ));

    return translator;
  }

  @Override
  protected void assertObjectsEqual(ExcelTranslator expected, ExcelTranslator actual) {
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getUUID(), actual.getUUID());

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

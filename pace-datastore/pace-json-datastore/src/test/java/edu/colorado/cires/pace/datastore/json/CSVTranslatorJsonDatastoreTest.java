package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.data.CSVTranslator;
import edu.colorado.cires.pace.data.CSVTranslatorField;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

class CSVTranslatorJsonDatastoreTest extends JsonDatastoreTest<CSVTranslator, String> {

  @Override
  protected Class<CSVTranslator> getClazz() {
    return CSVTranslator.class;
  }

  @Override
  protected JsonDatastore<CSVTranslator, String> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new CSVTranslatorJsonDatastore(storagePath, objectMapper);
  }

  @Override
  protected UUIDProvider<CSVTranslator> createUUIDProvider() {
    return CSVTranslator::getUUID;
  }

  @Override
  protected UniqueFieldProvider<CSVTranslator, String> createUniqueFieldProvider() {
    return CSVTranslator::getName;
  }

  @Override
  protected CSVTranslator createNewObject() {
    CSVTranslator translation = new CSVTranslator();
    
    translation.setName(UUID.randomUUID().toString());
    translation.setUUID(UUID.randomUUID());

    CSVTranslatorField field1 = new CSVTranslatorField();
    field1.setColumnNumber(1);
    field1.setPropertyName("property1");
    
    CSVTranslatorField field2 = new CSVTranslatorField();
    field2.setColumnNumber(2);
    field2.setPropertyName("property2");
    
    translation.setFields(List.of(
        field1, field2
    ));
    
    return translation;
  }

  @Override
  protected void assertObjectsEqual(CSVTranslator expected, CSVTranslator actual) {
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getUUID(), actual.getUUID());
    
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

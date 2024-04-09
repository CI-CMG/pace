package edu.colorado.cires.pace.core.state.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.core.state.repository.CSVTranslatorRepository;
import edu.colorado.cires.pace.data.CSVTranslator;
import edu.colorado.cires.pace.data.CSVTranslatorField;
import java.util.List;
import java.util.UUID;

class CSVTranslatorServiceTest extends CrudServiceTest<CSVTranslator, CSVTranslatorRepository> {

  @Override
  protected Class<CSVTranslatorRepository> getRepositoryClass() {
    return CSVTranslatorRepository.class;
  }

  @Override
  protected CRUDService<CSVTranslator> createService(CSVTranslatorRepository repository) {
    return new CSVTranslatorService(repository);
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

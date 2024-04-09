package edu.colorado.cires.pace.core.state.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.CSVTranslator;
import edu.colorado.cires.pace.data.CSVTranslatorField;
import java.util.List;

class CSVTranslatorRepositoryTest extends CrudRepositoryTest<CSVTranslator> {

  @Override
  protected CRUDRepository<CSVTranslator> createRepository() {
    return new CSVTranslatorRepository(createDatastore());
  }

  @Override
  protected CSVTranslator createNewObject(int suffix) {
    CSVTranslatorField field1 = new CSVTranslatorField(
        "property1",
        1
    );
    CSVTranslatorField field2 = new CSVTranslatorField(
        "property2",
        2
    );

    return new CSVTranslator(
        null,
        String.format("name-%s", suffix),
        List.of(
            field1, field2
        )
    );
  }

  @Override
  protected CSVTranslator copyWithUpdatedUniqueField(CSVTranslator object, String uniqueField) {
    return new CSVTranslator(
        object.uuid(),
        uniqueField,
        object.fields()
    );
  }

  @Override
  protected void assertObjectsEqual(CSVTranslator expected, CSVTranslator actual, boolean checkUUID) {
    assertEquals(expected.name(), actual.name());
    if (checkUUID) {
      assertEquals(expected.uuid(), actual.uuid());
    }

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

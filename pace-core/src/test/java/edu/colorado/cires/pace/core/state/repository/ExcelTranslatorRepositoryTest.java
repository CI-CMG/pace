package edu.colorado.cires.pace.core.state.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.ExcelTranslator;
import edu.colorado.cires.pace.data.ExcelTranslatorField;
import java.util.List;

public class ExcelTranslatorRepositoryTest extends CrudRepositoryTest<ExcelTranslator> {

  @Override
  protected CRUDRepository<ExcelTranslator> createRepository() {
    return new ExcelTranslatorRepository(createDatastore());
  }

  @Override
  protected ExcelTranslator createNewObject(int suffix) {
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
        null,
        String.format("name-%s", suffix),
        List.of(
            field1, field2
        )
    );
  }

  @Override
  protected ExcelTranslator copyWithUpdatedUniqueField(ExcelTranslator object, String uniqueField) {
    return new ExcelTranslator(
        object.uuid(),
        uniqueField,
        object.fields()
    );
  }

  @Override
  protected void assertObjectsEqual(ExcelTranslator expected, ExcelTranslator actual, boolean checkUUID) {
    assertEquals(expected.name(), actual.name());
    if (checkUUID) {
      assertEquals(expected.uuid(), actual.uuid());
    }

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

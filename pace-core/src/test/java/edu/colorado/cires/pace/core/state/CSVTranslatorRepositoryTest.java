package edu.colorado.cires.pace.core.state;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.CSVTranslator;
import edu.colorado.cires.pace.data.object.CSVTranslatorField;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.util.List;

class CSVTranslatorRepositoryTest extends CrudRepositoryTest<CSVTranslator> {

  @Override
  protected CRUDRepository<CSVTranslator> createRepository() {
    return new CSVTranslatorRepository(createDatastore());
  }

  @Override
  protected CSVTranslator createNewObject(int suffix) throws ValidationException {
    CSVTranslatorField field1 = CSVTranslatorField.builder()
        .propertyName("property1")
        .columnNumber(1)
        .build();
    CSVTranslatorField field2 = CSVTranslatorField.builder()
        .propertyName("property2")
        .columnNumber(2)
        .build();

    return CSVTranslator.builder()
        .name(String.format("name-%s", suffix))
        .fields(List.of(
            field1, field2
        )).build();
  }

  @Override
  protected CSVTranslator copyWithUpdatedUniqueField(CSVTranslator object, String uniqueField) throws ValidationException {
    return CSVTranslator.builder()
        .uuid(object.getUuid())
        .name(uniqueField)
        .fields(object.getFields())
        .build();
  }

  @Override
  protected void assertObjectsEqual(CSVTranslator expected, CSVTranslator actual, boolean checkUUID) {
    assertEquals(expected.getName(), actual.getName());
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }

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

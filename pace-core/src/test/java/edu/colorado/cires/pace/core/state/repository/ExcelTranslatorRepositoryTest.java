package edu.colorado.cires.pace.core.state.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.ExcelTranslator;
import edu.colorado.cires.pace.data.ExcelTranslatorField;
import java.util.List;

public class ExcelTranslatorRepositoryTest extends CrudRepositoryTest<ExcelTranslator, String> {

  @Override
  protected UUIDProvider<ExcelTranslator> getUUIDPRovider() {
    return ExcelTranslator::getUUID;
  }

  @Override
  protected UniqueFieldProvider<ExcelTranslator, String> getUniqueFieldProvider() {
    return ExcelTranslator::getName;
  }

  @Override
  protected UUIDSetter<ExcelTranslator> getUUIDSetter() {
    return ExcelTranslator::setUUID;
  }

  @Override
  protected UniqueFieldSetter<ExcelTranslator, String> getUniqueFieldSetter() {
    return ExcelTranslator::setName;
  }

  @Override
  protected CRUDRepository<ExcelTranslator, String> createRepository() {
    return new ExcelTranslatorRepository(createDatastore());
  }

  @Override
  protected ExcelTranslator createNewObject(int suffix) {
    ExcelTranslator translator = new ExcelTranslator();

    translator.setName(String.format("name-%s", suffix));

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

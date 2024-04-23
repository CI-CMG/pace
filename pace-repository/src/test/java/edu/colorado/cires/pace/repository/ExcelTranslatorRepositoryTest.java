package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.ExcelTranslator;
import edu.colorado.cires.pace.data.object.ExcelTranslatorField;
import edu.colorado.cires.pace.data.object.ExcelTranslatorFieldImpl;
import java.util.List;
import java.util.function.Function;

class ExcelTranslatorRepositoryTest extends CrudRepositoryTest<ExcelTranslator> {

  @Override
  protected CRUDRepository<ExcelTranslator> createRepository() {
    return new ExcelTranslatorRepository(createDatastore());
  }

  @Override
  protected Function<ExcelTranslator, String> uniqueFieldGetter() {
    return ExcelTranslator::getName;
  }

  @Override
  protected ExcelTranslator createNewObject(int suffix) {
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
        .name(String.format("name-%s", suffix))
        .fields(List.of(
            field1, field2
        )).build();
  }

  @Override
  protected ExcelTranslator copyWithUpdatedUniqueField(ExcelTranslator object, String uniqueField) {
    return ExcelTranslator.builder()
        .uuid(object.getUuid())
        .name(uniqueField)
        .fields(object.getFields())
        .build();
  }

  @Override
  protected void assertObjectsEqual(ExcelTranslator expected, ExcelTranslator actual, boolean checkUUID) {
    assertEquals(expected.getName(), actual.getName());
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }

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

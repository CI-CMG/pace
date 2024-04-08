package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.data.ExcelTranslator;
import edu.colorado.cires.pace.data.ExcelTranslatorField;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class ExcelTranslatorControllerTest extends CRUDControllerTest<ExcelTranslator, String> {

  @Override
  protected CRUDController<ExcelTranslator, String> createController(Datastore<ExcelTranslator, String> datastore) throws Exception {
    return new ExcelTranslatorController(datastore);
  }

  @Override
  protected UniqueFieldProvider<ExcelTranslator, String> getUniqueFieldProvider() {
    return ExcelTranslator::getName;
  }

  @Override
  protected UUIDProvider<ExcelTranslator> getUuidProvider() {
    return ExcelTranslator::getUUID;
  }

  @Override
  protected UniqueFieldSetter<ExcelTranslator, String> getUniqueFieldSetter() {
    return ExcelTranslator::setName;
  }

  @Override
  protected Supplier<String> getUniqueFieldName() {
    return () -> "name";
  }

  @Override
  protected ExcelTranslator createNewObject(boolean withUUID) {
    ExcelTranslator translator = new ExcelTranslator();

    translator.setName(UUID.randomUUID().toString());

    if (withUUID) {
      translator.setUUID(UUID.randomUUID());
    }

    ExcelTranslatorField field1 = new ExcelTranslatorField();
    field1.setColumnNumber(1);
    field1.setSheetNumber(1);
    field1.setPropertyName("property1");

    ExcelTranslatorField field2 = new ExcelTranslatorField();
    field2.setColumnNumber(2);
    field2.setSheetNumber(1);
    field2.setPropertyName("property2");

    translator.setFields(List.of(
        field1, field2
    ));

    return translator;
  }
}

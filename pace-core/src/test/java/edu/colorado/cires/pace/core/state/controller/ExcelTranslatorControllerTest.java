package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.ExcelTranslator;
import edu.colorado.cires.pace.data.ExcelTranslatorField;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class ExcelTranslatorControllerTest extends CRUDControllerTest<ExcelTranslator> {

  @Override
  protected CRUDController<ExcelTranslator> createController(Datastore<ExcelTranslator> datastore) throws Exception {
    return new ExcelTranslatorController(datastore);
  }

  @Override
  protected Supplier<String> getUniqueFieldName() {
    return () -> "name";
  }

  @Override
  protected ExcelTranslator createNewObject(boolean withUUID) {
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
        !withUUID ? null : UUID.randomUUID(),
        UUID.randomUUID().toString(),
        List.of(
            field1, field2
        )
    );
  }

  @Override
  protected ExcelTranslator setUniqueField(ExcelTranslator object, String uniqueField) {
    return new ExcelTranslator(
        object.uuid(),
        uniqueField,
        object.fields()
    );
  }
}

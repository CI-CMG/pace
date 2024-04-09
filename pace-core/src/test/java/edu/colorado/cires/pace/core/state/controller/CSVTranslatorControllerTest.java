package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.CSVTranslator;
import edu.colorado.cires.pace.data.CSVTranslatorField;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

class CSVTranslatorControllerTest extends CRUDControllerTest<CSVTranslator> {

  @Override
  protected CRUDController<CSVTranslator> createController(Datastore<CSVTranslator> datastore) throws Exception {
    return new CSVTranslatorController(datastore);
  }

  @Override
  protected Supplier<String> getUniqueFieldName() {
    return () -> "name";
  }

  @Override
  protected CSVTranslator createNewObject(boolean withUUID) {
    CSVTranslatorField field1 = new CSVTranslatorField(
        "property1",
        1
    );
    CSVTranslatorField field2 = new CSVTranslatorField(
        "property2",
        2
    );

    return new CSVTranslator(
        !withUUID ? null : UUID.randomUUID(),
        UUID.randomUUID().toString(),
        List.of(
            field1, field2
        )
    );
  }

  @Override
  protected CSVTranslator setUniqueField(CSVTranslator object, String uniqueField) {
    return new CSVTranslator(
        object.uuid(),
        uniqueField,
        object.fields()
    );
  }
}

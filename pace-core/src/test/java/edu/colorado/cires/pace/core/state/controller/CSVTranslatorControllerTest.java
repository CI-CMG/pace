package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.data.CSVTranslator;
import edu.colorado.cires.pace.data.CSVTranslatorField;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

class CSVTranslatorControllerTest extends CRUDControllerTest<CSVTranslator, String> {

  @Override
  protected CRUDController<CSVTranslator, String> createController(Datastore<CSVTranslator, String> datastore) throws Exception {
    return new CSVTranslatorController(datastore);
  }

  @Override
  protected UniqueFieldProvider<CSVTranslator, String> getUniqueFieldProvider() {
    return CSVTranslator::getName;
  }

  @Override
  protected UUIDProvider<CSVTranslator> getUuidProvider() {
    return CSVTranslator::getUUID;
  }

  @Override
  protected UniqueFieldSetter<CSVTranslator, String> getUniqueFieldSetter() {
    return CSVTranslator::setName;
  }

  @Override
  protected Supplier<String> getUniqueFieldName() {
    return () -> "name";
  }

  @Override
  protected CSVTranslator createNewObject(boolean withUUID) {
    CSVTranslator translation = new CSVTranslator();

    translation.setName(UUID.randomUUID().toString());
    
    if (withUUID) {
      translation.setUUID(UUID.randomUUID());
    }

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
}

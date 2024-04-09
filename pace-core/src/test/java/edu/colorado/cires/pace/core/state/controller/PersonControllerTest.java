package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.Person;
import java.util.UUID;
import java.util.function.Supplier;

class PersonControllerTest extends CRUDControllerTest<Person> {

  @Override
  protected CRUDController<Person> createController(Datastore<Person> datastore) {
    return new PersonController(datastore);
  }

  @Override
  protected Supplier<String> getUniqueFieldName() {
    return () -> "name";
  }

  @Override
  protected Person createNewObject(boolean withUUID) {
    return new Person(
        !withUUID ? null : UUID.randomUUID(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString()
    );
  }

  @Override
  protected Person setUniqueField(Person object, String uniqueField) {
    return new Person(
        object.uuid(),
        uniqueField,
        object.organization(),
        object.position(),
        object.street(),
        object.city(),
        object.state(),
        object.zip(),
        object.country(),
        object.email(),
        object.phone(),
        object.orcid()
    );
  }
}

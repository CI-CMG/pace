package edu.colorado.cires.pace.core.repository;

import edu.colorado.cires.pace.core.datastore.Datastore;
import edu.colorado.cires.pace.data.Person;

public class PersonRepository extends CRUDRepository<Person, String> {

  public PersonRepository(Datastore<Person, String> datastore) {
    super(Person::getUUID, Person::getName, Person::setUUID, datastore);
  }

  @Override
  protected String getObjectName() {
    return Person.class.getSimpleName();
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }
}

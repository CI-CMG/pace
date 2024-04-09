package edu.colorado.cires.pace.core.state.repository;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.Person;

public class PersonRepository extends CRUDRepository<Person> {

  public PersonRepository(Datastore<Person> datastore) {
    super(datastore);
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

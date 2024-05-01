package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.Person;
import edu.colorado.cires.pace.datastore.Datastore;
import java.util.UUID;

public class PersonRepository extends CRUDRepository<Person> {

  public PersonRepository(Datastore<Person> datastore) {
    super(datastore, true);
  }

  @Override
  protected Person setUUID(Person object, UUID uuid) {
    return object.toBuilder()
        .uuid(uuid)
        .build();
  }

}

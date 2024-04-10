package edu.colorado.cires.pace.core.state;

import edu.colorado.cires.pace.data.object.Person;
import java.util.UUID;

public class PersonRepository extends CRUDRepository<Person> {

  public PersonRepository(Datastore<Person> datastore) {
    super(datastore);
  }

  @Override
  protected Person setUUID(Person object, UUID uuid) {
    return object.toBuilder()
        .uuid(uuid)
        .build();
  }

}

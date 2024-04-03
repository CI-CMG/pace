package edu.colorado.cires.pace.core.service;

import edu.colorado.cires.pace.core.repository.CRUDRepository;
import edu.colorado.cires.pace.data.Person;
import java.util.function.Consumer;

public class PersonService extends CRUDService<Person, String> {

  protected PersonService(CRUDRepository<Person, String> personRepository,
      Consumer<Person> onSuccess, Consumer<Exception> onFailure) {
    super(personRepository, onSuccess, onFailure);
  }
}

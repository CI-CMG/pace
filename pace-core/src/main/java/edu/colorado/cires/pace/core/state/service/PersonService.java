package edu.colorado.cires.pace.core.state.service;

import edu.colorado.cires.pace.core.state.repository.CRUDRepository;
import edu.colorado.cires.pace.data.Person;

public class PersonService extends CRUDService<Person> {

  public PersonService(CRUDRepository<Person> personRepository) {
    super(personRepository);
  }
}

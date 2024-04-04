package edu.colorado.cires.pace.core.service;

import edu.colorado.cires.pace.core.repository.CRUDRepository;
import edu.colorado.cires.pace.data.Person;

public class PersonService extends CRUDService<Person, String> {

  public PersonService(CRUDRepository<Person, String> personRepository) {
    super(personRepository);
  }
}

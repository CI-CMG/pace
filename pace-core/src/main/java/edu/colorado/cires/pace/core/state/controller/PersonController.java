package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.core.state.repository.PersonRepository;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.core.state.service.PersonService;
import edu.colorado.cires.pace.core.validation.PersonValidator;
import edu.colorado.cires.pace.core.validation.Validator;
import edu.colorado.cires.pace.data.Person;

public class PersonController extends CRUDController<Person> {

  @Override
  protected Validator<Person> getValidator() {
    return new PersonValidator();
  }

  @Override
  protected CRUDService<Person> createService(Datastore<Person> datastore, Datastore<?>... additionalDataStores) {
    return new PersonService(
        new PersonRepository(datastore)
    );
  }

  public PersonController(Datastore<Person> datastore) {
    super(datastore);
  }
}

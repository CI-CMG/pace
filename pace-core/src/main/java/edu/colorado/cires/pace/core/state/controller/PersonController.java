package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.core.validation.PersonValidator;
import edu.colorado.cires.pace.core.validation.Validator;
import edu.colorado.cires.pace.data.Person;

public class PersonController extends CRUDController<Person, String> {

  @Override
  protected Validator<Person> getValidator() {
    return new PersonValidator();
  }

  public PersonController(CRUDService<Person, String> service) {
    super(service);
  }
}

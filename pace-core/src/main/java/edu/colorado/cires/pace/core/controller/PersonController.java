package edu.colorado.cires.pace.core.controller;

import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.service.CRUDService;
import edu.colorado.cires.pace.data.Person;

public class PersonController extends CRUDController<Person, String> {

  protected PersonController(CRUDService<Person, String> service,
      Validator<Person> validator) {
    super(service, validator);
  }
}

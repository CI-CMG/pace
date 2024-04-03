package edu.colorado.cires.pace.core.controller;

import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.repository.UUIDProvider;
import edu.colorado.cires.pace.core.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.core.service.CRUDService;
import edu.colorado.cires.pace.data.Person;
import java.util.UUID;

class PersonControllerTest extends CRUDControllerTest<Person, String> {

  @Override
  protected CRUDController<Person, String> createController(CRUDService<Person, String> service, Validator<Person> validator) {
    return new PersonController(service, validator);
  }

  @Override
  protected UniqueFieldProvider<Person, String> getUniqueFieldProvider() {
    return Person::getName;
  }

  @Override
  protected UUIDProvider<Person> getUuidProvider() {
    return Person::getUUID;
  }

  @Override
  protected Person createNewObject() {
    Person person = new Person();
    person.setCity(UUID.randomUUID().toString());
    person.setCountry(UUID.randomUUID().toString());
    person.setEmail(UUID.randomUUID().toString());
    person.setName(UUID.randomUUID().toString());
    person.setOrcid(UUID.randomUUID().toString());
    person.setOrganization(UUID.randomUUID().toString());
    person.setPhone(UUID.randomUUID().toString());
    person.setPosition(UUID.randomUUID().toString());
    person.setState(UUID.randomUUID().toString());
    person.setStreet(UUID.randomUUID().toString());
    person.setUse(true);
    person.setUUID(UUID.randomUUID());
    person.setZip(UUID.randomUUID().toString());
    return person;
  }
}

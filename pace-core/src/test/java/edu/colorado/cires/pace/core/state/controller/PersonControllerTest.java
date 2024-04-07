package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.data.Person;
import java.util.UUID;
import java.util.function.Supplier;

class PersonControllerTest extends CRUDControllerTest<Person, String> {

  @Override
  protected CRUDController<Person, String> createController(CRUDService<Person, String> service) {
    return new PersonController(service);
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
  protected UniqueFieldSetter<Person, String> getUniqueFieldSetter() {
    return Person::setName;
  }

  @Override
  protected Supplier<String> getUniqueFieldName() {
    return () -> "name";
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

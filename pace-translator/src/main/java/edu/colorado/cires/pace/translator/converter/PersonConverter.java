package edu.colorado.cires.pace.translator.converter;

import static edu.colorado.cires.pace.translator.converter.ConversionUtils.stringFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.uuidFromMap;

import edu.colorado.cires.pace.data.object.contact.person.Person;
import edu.colorado.cires.pace.data.object.contact.person.translator.PersonTranslator;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;

public class PersonConverter extends Converter<PersonTranslator, Person> {

  @Override
  public Person convert(PersonTranslator translator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return Person.builder()
        .uuid(uuidFromMap(properties, translator.getPersonUUID(), row, runtimeException))
        .name(stringFromMap(properties, translator.getPersonName()))
        .organization(stringFromMap(properties, translator.getOrganization()))
        .position(stringFromMap(properties, translator.getPosition()))
        .street(stringFromMap(properties, translator.getStreet()))
        .city(stringFromMap(properties, translator.getCity()))
        .state(stringFromMap(properties, translator.getState()))
        .zip(stringFromMap(properties, translator.getZip()))
        .country(stringFromMap(properties, translator.getCountry()))
        .email(stringFromMap(properties, translator.getEmail()))
        .phone(stringFromMap(properties, translator.getPhone()))
        .orcid(stringFromMap(properties, translator.getOrcid()))
        .build();
  }
}

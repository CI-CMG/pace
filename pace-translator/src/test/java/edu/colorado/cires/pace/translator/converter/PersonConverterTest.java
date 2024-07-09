package edu.colorado.cires.pace.translator.converter;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.object.Person;
import edu.colorado.cires.pace.data.translator.PersonTranslator;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PersonConverterTest {
  
  private final Converter<PersonTranslator, Person> converter = new PersonConverter();

  @Test
  void convert() throws TranslationException {
    UUID uuid = UUID.randomUUID();
    String name = "name-value";
    String organization = "organization-value";
    String position = "position-value";
    String street = "street-value";
    String city = "city-value";
    String state = "state-value";
    String zip = "zip-value";
    String country = "country-value";
    String email = "email-value";
    String phone = "phone-value";
    String orcid = "orcid-value";
    
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("person-uuid", new ValueWithColumnNumber(Optional.of(uuid.toString()), 1));
    map.put("person-name", new ValueWithColumnNumber(Optional.of(name), 2));
    map.put("person-organization", new ValueWithColumnNumber(Optional.of(organization), 3));
    map.put("person-position", new ValueWithColumnNumber(Optional.of(position), 4));
    map.put("person-street", new ValueWithColumnNumber(Optional.of(street), 5));
    map.put("person-city", new ValueWithColumnNumber(Optional.of(city), 6));
    map.put("person-state", new ValueWithColumnNumber(Optional.of(state), 7));
    map.put("person-zip", new ValueWithColumnNumber(Optional.of(zip), 8));
    map.put("person-country", new ValueWithColumnNumber(Optional.of(country), 9));
    map.put("person-email", new ValueWithColumnNumber(Optional.of(email), 10));
    map.put("person-phone", new ValueWithColumnNumber(Optional.of(phone), 11));
    map.put("person-orcid", new ValueWithColumnNumber(Optional.of(orcid), 12));
    
    Person person = converter.convert(
        PersonTranslator.builder()
            .personUUID("person-uuid")
            .personName("person-name")
            .organization("person-organization")
            .position("person-position")
            .street("person-street")
            .city("person-city")
            .state("person-state")
            .zip("person-zip")
            .country("person-country")
            .email("person-email")
            .phone("person-phone")
            .orcid("person-orcid")
            .build(),
        map,
        1,
        new RuntimeException()
    );
    
    assertEquals(uuid, person.getUuid());
    assertEquals(name, person.getName());
    assertEquals(organization, person.getOrganization());
    assertEquals(position, person.getPosition());
    assertEquals(street, person.getStreet());
    assertEquals(city, person.getCity());
    assertEquals(state, person.getState());
    assertEquals(zip, person.getZip());
    assertEquals(country, person.getCountry());
    assertEquals(email, person.getEmail());
    assertEquals(phone, person.getPhone());
    assertEquals(orcid, person.getOrcid());
  }
}
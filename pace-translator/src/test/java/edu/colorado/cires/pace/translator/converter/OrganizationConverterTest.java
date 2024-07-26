package edu.colorado.cires.pace.translator.converter;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.object.contact.organization.Organization;
import edu.colorado.cires.pace.data.object.contact.organization.translator.OrganizationTranslator;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OrganizationConverterTest {
  
  private final Converter<OrganizationTranslator, Organization> converter = new OrganizationConverter();

  @Test
  void convert() throws TranslationException {
    UUID uuid = UUID.randomUUID();
    String name = "name-value";
    String street = "street-value";
    String city = "city-value";
    String state = "state-value";
    String zip = "zip-value";
    String country = "country-value";
    String email = "email-value";
    String phone = "phone-value";
    
    Organization organization = converter.convert(
        OrganizationTranslator.builder()
            .organizationUUID("organization-uuid")
            .organizationName("organization-name")
            .street("organization-street")
            .city("organization-city")
            .state("organization-state")
            .zip("organization-zip")
            .country("organization-country")
            .email("organization-email")
            .phone("organization-phone")
            .build(),
        Map.of(
            "organization-uuid", new ValueWithColumnNumber(Optional.of(uuid.toString()), 1),
            "organization-name", new ValueWithColumnNumber(Optional.of(name), 2),
            "organization-street", new ValueWithColumnNumber(Optional.of(street), 3),
            "organization-city", new ValueWithColumnNumber(Optional.of(city), 4),
            "organization-state", new ValueWithColumnNumber(Optional.of(state), 5),
            "organization-zip", new ValueWithColumnNumber(Optional.of(zip), 6),
            "organization-country", new ValueWithColumnNumber(Optional.of(country), 7),
            "organization-email", new ValueWithColumnNumber(Optional.of(email), 8),
            "organization-phone", new ValueWithColumnNumber(Optional.of(phone), 9)
        ),
        1,
        new RuntimeException()
    );
    
    assertEquals(uuid, organization.getUuid());
    assertEquals(name, organization.getName());
    assertEquals(street, organization.getStreet());
    assertEquals(city, organization.getCity());
    assertEquals(state, organization.getState());
    assertEquals(zip, organization.getZip());
    assertEquals(country, organization.getCountry());
    assertEquals(email, organization.getEmail());
    assertEquals(phone, organization.getPhone());
  }
}
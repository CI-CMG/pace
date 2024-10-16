package edu.colorado.cires.pace.cli.command.organization;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.ObjectWithEditableUUIDCommandTest;
import edu.colorado.cires.pace.data.object.contact.organization.Organization;
import edu.colorado.cires.pace.data.object.contact.organization.translator.OrganizationTranslator;
import java.util.List;
import java.util.UUID;

public class OrganizationCommandTest extends ObjectWithEditableUUIDCommandTest<Organization, OrganizationTranslator> {

  @Override
  public Organization createObject(String uniqueField, boolean withUUID) {
    return Organization.builder()
        .uuid(withUUID ? UUID.randomUUID() : null)
        .name(uniqueField)
        .street("street")
        .city("city")
        .state("state")
        .zip("zip")
        .country("country")
        .email("email")
        .phone("phone")
        .build();
  }

  @Override
  protected String getRepositoryDirectory() {
    return "organizations";
  }

  @Override
  protected String getCommandPrefix() {
    return "organization";
  }

  @Override
  protected TypeReference<List<Organization>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected Class<Organization> getClazz() {
    return Organization.class;
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected void assertObjectsEqual(Organization expected, Organization actual, boolean checkUUID) {
    assertOrganizationsEqual(expected, actual, checkUUID);
  }

  public static void assertOrganizationsEqual(Organization expected, Organization actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    } else {
      assertNotNull(actual.getUuid());
    }

    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getStreet(), actual.getStreet());
    assertEquals(expected.getCity(), actual.getCity());
    assertEquals(expected.getState(), actual.getState());
    assertEquals(expected.getZip(), actual.getZip());
    assertEquals(expected.getCountry(), actual.getCountry());
    assertEquals(expected.getEmail(), actual.getEmail());
    assertEquals(expected.getPhone(), actual.getPhone());
  }

  @Override
  protected String getUniqueField(Organization object) {
    return object.getName();
  }

  @Override
  protected Organization updateObject(Organization original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected String[] getTranslatorFields() {
    return new String[] {
        "UUID",
        "organizationName",
        "street",
        "city",
        "state",
        "zip",
        "country",
        "email",
        "phone"
    };
  }

  @Override
  protected OrganizationTranslator createTranslator(String name) {
    return OrganizationTranslator.builder()
        .name(name)
        .organizationUUID("UUID")
        .organizationName("organizationName")
        .street("street")
        .city("city")
        .state("state")
        .zip("zip")
        .country("country")
        .email("email")
        .phone("phone")
        .build();
  }

  @Override
  protected String[] objectToRow(Organization object) {
    return new String[] {
        object.getUuid() == null ? "" : object.getUuid().toString(),
        object.getName(),
        object.getStreet(),
        object.getCity(),
        object.getState(),
        object.getZip(),
        object.getCountry(),
        object.getEmail(),
        object.getPhone()
    };
  }
}
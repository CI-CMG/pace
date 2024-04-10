package edu.colorado.cires.pace.core.state;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.data.validation.ValidationException;

class OrganizationRepositoryTest extends CrudRepositoryTest<Organization> {

  @Override
  protected CRUDRepository<Organization> createRepository() {
    return new OrganizationRepository(createDatastore());
  }

  @Override
  protected Organization createNewObject(int suffix) throws ValidationException {
    return Organization.builder()
        .name(String.format("name-%s", suffix))
        .street(String.format("street-%s", suffix))
        .city(String.format("city-%s", suffix))
        .state(String.format("state-%s", suffix))
        .zip(String.format("zip-%s", suffix))
        .country(String.format("country-%s", suffix))
        .email(String.format("email-%s", suffix))
        .phone(String.format("phone-%s", suffix))
        .build();
  }

  @Override
  protected Organization copyWithUpdatedUniqueField(Organization object, String uniqueField) throws ValidationException {
    return Organization.builder()
        .uuid(object.getUuid())
        .name(uniqueField)
        .street(object.getStreet())
        .city(object.getCity())
        .state(object.getState())
        .zip(object.getZip())
        .country(object.getCountry())
        .email(object.getEmail())
        .phone(object.getPhone())
        .build();
  }

  @Override
  protected void assertObjectsEqual(Organization expected, Organization actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
    assertEquals(expected.getCity(), actual.getCity());
    assertEquals(expected.getCountry(), actual.getCountry());
    assertEquals(expected.getEmail(), actual.getEmail());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getPhone(), actual.getPhone());
    assertEquals(expected.getState(), actual.getState());
    assertEquals(expected.getStreet(), actual.getStreet());
    assertEquals(expected.getZip(), actual.getZip());
  }
}

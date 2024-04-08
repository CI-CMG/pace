package edu.colorado.cires.pace.core.state.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.Organization;

class OrganizationRepositoryTest extends CrudRepositoryTest<Organization, String> {

  @Override
  protected UUIDProvider<Organization> getUUIDPRovider() {
    return Organization::getUUID;
  }

  @Override
  protected UniqueFieldProvider<Organization, String> getUniqueFieldProvider() {
    return Organization::getName;
  }

  @Override
  protected UUIDSetter<Organization> getUUIDSetter() {
    return Organization::setUUID;
  }

  @Override
  protected UniqueFieldSetter<Organization, String> getUniqueFieldSetter() {
    return Organization::setName;
  }

  @Override
  protected CRUDRepository<Organization, String> createRepository() {
    return new OrganizationRepository(createDatastore());
  }

  @Override
  protected Organization createNewObject(int suffix) {
    Organization organization = new Organization();
    organization.setCity(String.format("city-%s", suffix));
    organization.setCountry(String.format("country-%s", suffix));
    organization.setEmail(String.format("email-%s", suffix));
    organization.setName(String.format("name-%s", suffix));
    organization.setPhone(String.format("phone-%s", suffix));
    organization.setState(String.format("state-%s", suffix));
    organization.setStreet(String.format("street-%s", suffix));
    organization.setUse(true);
    organization.setZip(String.format("zip-%s", suffix));
    return organization;
  }

  @Override
  protected void assertObjectsEqual(Organization expected, Organization actual) {
    assertEquals(expected.getCity(), actual.getCity());
    assertEquals(expected.getCountry(), actual.getCountry());
    assertEquals(expected.getEmail(), actual.getEmail());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getPhone(), actual.getPhone());
    assertEquals(expected.getState(), actual.getState());
    assertEquals(expected.getStreet(), actual.getStreet());
    assertEquals(expected.getUse(), actual.getUse());
    assertEquals(expected.getZip(), actual.getZip());
  }
}
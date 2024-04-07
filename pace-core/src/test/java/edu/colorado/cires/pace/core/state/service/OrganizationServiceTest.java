package edu.colorado.cires.pace.core.state.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.core.state.repository.OrganizationRepository;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.data.Organization;
import java.util.UUID;

class OrganizationServiceTest extends CrudServiceTest<Organization, String, OrganizationRepository> {

  @Override
  protected Class<OrganizationRepository> getRepositoryClass() {
    return OrganizationRepository.class;
  }

  @Override
  protected UniqueFieldProvider<Organization, String> getUniqueFieldProvider() {
    return Organization::getName;
  }

  @Override
  protected UUIDProvider<Organization> getUUIDProvider() {
    return Organization::getUUID;
  }

  @Override
  protected CRUDService<Organization, String> createService(OrganizationRepository repository) {
    return new OrganizationService(repository);
  }

  @Override
  protected Organization createNewObject() {
    Organization organization = new Organization();
    organization.setCity(UUID.randomUUID().toString());
    organization.setCountry(UUID.randomUUID().toString());
    organization.setEmail(UUID.randomUUID().toString());
    organization.setName(UUID.randomUUID().toString());
    organization.setPhone(UUID.randomUUID().toString());
    organization.setState(UUID.randomUUID().toString());
    organization.setStreet(UUID.randomUUID().toString());
    organization.setUse(true);
    organization.setUUID(UUID.randomUUID());
    organization.setZip(UUID.randomUUID().toString());
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

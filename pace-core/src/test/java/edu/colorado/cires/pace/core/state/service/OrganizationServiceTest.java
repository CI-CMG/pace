package edu.colorado.cires.pace.core.state.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.core.state.repository.OrganizationRepository;
import edu.colorado.cires.pace.data.Organization;
import java.util.UUID;

class OrganizationServiceTest extends CrudServiceTest<Organization, OrganizationRepository> {

  @Override
  protected Class<OrganizationRepository> getRepositoryClass() {
    return OrganizationRepository.class;
  }

  @Override
  protected CRUDService<Organization> createService(OrganizationRepository repository) {
    return new OrganizationService(repository);
  }

  @Override
  protected Organization createNewObject() {
    return new Organization(
        UUID.randomUUID(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString()
    );
  }

  @Override
  protected void assertObjectsEqual(Organization expected, Organization actual) {
    assertEquals(expected.uuid(), actual.uuid());
    assertEquals(expected.city(), actual.city());
    assertEquals(expected.country(), actual.country());
    assertEquals(expected.email(), actual.email());
    assertEquals(expected.name(), actual.name());
    assertEquals(expected.phone(), actual.phone());
    assertEquals(expected.state(), actual.state());
    assertEquals(expected.street(), actual.street());
    assertEquals(expected.zip(), actual.zip());
  }
}

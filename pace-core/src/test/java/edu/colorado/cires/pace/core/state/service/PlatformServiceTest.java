package edu.colorado.cires.pace.core.state.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.core.state.repository.PlatformRepository;
import edu.colorado.cires.pace.data.Platform;
import java.util.UUID;

class PlatformServiceTest extends CrudServiceTest<Platform, PlatformRepository> {

  @Override
  protected Class<PlatformRepository> getRepositoryClass() {
    return PlatformRepository.class;
  }

  @Override
  protected CRUDService<Platform> createService(PlatformRepository repository) {
    return new PlatformService(repository);
  }

  @Override
  protected Platform createNewObject() {
    return new Platform(
        UUID.randomUUID(),
        UUID.randomUUID().toString()
    );
  }

  @Override
  protected void assertObjectsEqual(Platform expected, Platform actual) {
    assertEquals(expected.uuid(), actual.uuid());
    assertEquals(expected.name(), actual.name());
  }
}

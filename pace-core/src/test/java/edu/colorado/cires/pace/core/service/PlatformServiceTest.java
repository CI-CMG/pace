package edu.colorado.cires.pace.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.core.repository.PlatformRepository;
import edu.colorado.cires.pace.core.repository.UUIDProvider;
import edu.colorado.cires.pace.core.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.data.Platform;
import java.util.UUID;

class PlatformServiceTest extends CrudServiceTest<Platform, String, PlatformRepository> {

  @Override
  protected Class<PlatformRepository> getRepositoryClass() {
    return PlatformRepository.class;
  }

  @Override
  protected UniqueFieldProvider<Platform, String> getUniqueFieldProvider() {
    return Platform::getName;
  }

  @Override
  protected UUIDProvider<Platform> getUUIDProvider() {
    return Platform::getUUID;
  }

  @Override
  protected CRUDService<Platform, String> createService(PlatformRepository repository) {
    return new PlatformService(repository);
  }

  @Override
  protected Platform createNewObject() {
    Platform platform = new Platform();
    platform.setUUID(UUID.randomUUID());
    platform.setName(UUID.randomUUID().toString());
    platform.setUse(true);
    return platform;
  }

  @Override
  protected void assertObjectsEqual(Platform expected, Platform actual) {
    assertEquals(expected.getUUID(), actual.getUUID());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getUse(), actual.getUse());
  }
}

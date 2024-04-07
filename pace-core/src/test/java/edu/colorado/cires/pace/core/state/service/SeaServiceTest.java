package edu.colorado.cires.pace.core.state.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.core.state.repository.SeaRepository;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.data.Sea;
import java.util.UUID;

class SeaServiceTest extends CrudServiceTest<Sea, String, SeaRepository> {

  @Override
  protected Class<SeaRepository> getRepositoryClass() {
    return SeaRepository.class;
  }

  @Override
  protected UniqueFieldProvider<Sea, String> getUniqueFieldProvider() {
    return Sea::getName;
  }

  @Override
  protected UUIDProvider<Sea> getUUIDProvider() {
    return Sea::getUUID;
  }

  @Override
  protected CRUDService<Sea, String> createService(SeaRepository repository) {
    return new SeaService(repository);
  }

  @Override
  protected Sea createNewObject() {
    Sea sea = new Sea();
    sea.setUUID(UUID.randomUUID());
    sea.setUse(true);
    sea.setName(UUID.randomUUID().toString());
    return sea;
  }

  @Override
  protected void assertObjectsEqual(Sea expected, Sea actual) {
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getUUID(), actual.getUUID());
    assertEquals(expected.getUse(), actual.getUse());
  }
}

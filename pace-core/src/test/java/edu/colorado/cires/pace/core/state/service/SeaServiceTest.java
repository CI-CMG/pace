package edu.colorado.cires.pace.core.state.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.core.state.repository.SeaRepository;
import edu.colorado.cires.pace.data.Sea;
import java.util.UUID;

class SeaServiceTest extends CrudServiceTest<Sea, SeaRepository> {

  @Override
  protected Class<SeaRepository> getRepositoryClass() {
    return SeaRepository.class;
  }

  @Override
  protected CRUDService<Sea> createService(SeaRepository repository) {
    return new SeaService(repository);
  }

  @Override
  protected Sea createNewObject() {
    return new Sea(
        UUID.randomUUID(),
        UUID.randomUUID().toString()
    );
  }

  @Override
  protected void assertObjectsEqual(Sea expected, Sea actual) {
    assertEquals(expected.name(), actual.name());
    assertEquals(expected.uuid(), actual.uuid());
  }
}

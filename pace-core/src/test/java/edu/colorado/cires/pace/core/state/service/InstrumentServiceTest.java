package edu.colorado.cires.pace.core.state.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.core.state.repository.InstrumentRepository;
import edu.colorado.cires.pace.data.FileType;
import edu.colorado.cires.pace.data.Instrument;
import java.util.List;
import java.util.UUID;

class InstrumentServiceTest extends CrudServiceTest<Instrument, InstrumentRepository> {

  @Override
  protected Class<InstrumentRepository> getRepositoryClass() {
    return InstrumentRepository.class;
  }

  @Override
  protected CRUDService<Instrument> createService(InstrumentRepository repository) {
    return new InstrumentService(repository);
  }

  @Override
  protected Instrument createNewObject() {
    FileType fileType1 = new FileType(
        UUID.randomUUID(),
        UUID.randomUUID().toString(),
        "comment"
    );

    FileType fileType2 = new FileType(
        UUID.randomUUID(),
        UUID.randomUUID().toString(),
        "comment"
    );

    return new Instrument(
        UUID.randomUUID(),
        UUID.randomUUID().toString(),
        List.of(
            fileType1, fileType2
        )
    );
  }

  @Override
  protected void assertObjectsEqual(Instrument expected, Instrument actual) {
    assertEquals(expected.name(), actual. name());
    assertEquals(expected.uuid(), actual.uuid());
    assertEquals(expected.fileTypes(), actual.fileTypes());
  }
}

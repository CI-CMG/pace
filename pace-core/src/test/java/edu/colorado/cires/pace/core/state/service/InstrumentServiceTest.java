package edu.colorado.cires.pace.core.state.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.core.state.repository.InstrumentRepository;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.data.FileType;
import edu.colorado.cires.pace.data.Instrument;
import java.util.List;
import java.util.UUID;

class InstrumentServiceTest extends CrudServiceTest<Instrument, String, InstrumentRepository> {

  @Override
  protected Class<InstrumentRepository> getRepositoryClass() {
    return InstrumentRepository.class;
  }

  @Override
  protected UniqueFieldProvider<Instrument, String> getUniqueFieldProvider() {
    return Instrument::getName;
  }

  @Override
  protected UUIDProvider<Instrument> getUUIDProvider() {
    return Instrument::getUUID;
  }

  @Override
  protected CRUDService<Instrument, String> createService(InstrumentRepository repository) {
    return new InstrumentService(repository);
  }

  @Override
  protected Instrument createNewObject() {
    Instrument instrument = new Instrument();
    instrument.setUUID(UUID.randomUUID());
    instrument.setName(UUID.randomUUID().toString());
    instrument.setUse(true);

    FileType fileType1 = new FileType();
    fileType1.setUUID(UUID.randomUUID());
    fileType1.setType(UUID.randomUUID().toString());

    FileType fileType2 = new FileType();
    fileType2.setUUID(UUID.randomUUID());
    fileType2.setType(UUID.randomUUID().toString());

    instrument.setFileTypes(List.of(
        fileType1, fileType2
    ));

    return instrument;
  }

  @Override
  protected void assertObjectsEqual(Instrument expected, Instrument actual) {
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getUUID(), actual.getUUID());
    assertEquals(expected.getUse(), actual.getUse());
    assertEquals(expected.getFileTypes(), actual.getFileTypes());
  }
}
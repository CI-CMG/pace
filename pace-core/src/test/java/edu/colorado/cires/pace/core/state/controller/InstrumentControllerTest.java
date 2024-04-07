package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.data.FileType;
import edu.colorado.cires.pace.data.Instrument;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

class InstrumentControllerTest extends CRUDControllerTest<Instrument, String> {

  @Override
  protected CRUDController<Instrument, String> createController(CRUDService<Instrument, String> service) {
    return new InstrumentController(service);
  }

  @Override
  protected UniqueFieldProvider<Instrument, String> getUniqueFieldProvider() {
    return Instrument::getName;
  }

  @Override
  protected UUIDProvider<Instrument> getUuidProvider() {
    return Instrument::getUUID;
  }

  @Override
  protected UniqueFieldSetter<Instrument, String> getUniqueFieldSetter() {
    return Instrument::setName;
  }

  @Override
  protected Supplier<String> getUniqueFieldName() {
    return () -> "name";
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
}

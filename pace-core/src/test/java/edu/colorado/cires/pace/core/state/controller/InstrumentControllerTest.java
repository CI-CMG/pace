package edu.colorado.cires.pace.core.state.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.FileType;
import edu.colorado.cires.pace.data.Instrument;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

class InstrumentControllerTest extends CRUDControllerTest<Instrument> {

  @Override
  protected CRUDController<Instrument> createController(Datastore<Instrument> datastore) throws Exception {
    Datastore<FileType> fileTypeDatastore = mock(Datastore.class);
    when(fileTypeDatastore.findByUUID(any())).thenReturn(Optional.of(new FileType(UUID.randomUUID(), UUID.randomUUID().toString(), UUID.randomUUID().toString())));
    return new InstrumentController(datastore, fileTypeDatastore);
  }

  @Override
  protected Supplier<String> getUniqueFieldName() {
    return () -> "name";
  }

  @Override
  protected Instrument createNewObject(boolean withUUID) {
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
        !withUUID ? null : UUID.randomUUID(),
        UUID.randomUUID().toString(),
        List.of(
            fileType1, fileType2
        )
    );
  }

  @Override
  protected Instrument setUniqueField(Instrument object, String uniqueField) {
    return new Instrument(
        object.uuid(),
        uniqueField,
        object.fileTypes()
    );
  }
}

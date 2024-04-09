package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.FileType;
import java.util.UUID;
import java.util.function.Supplier;

class FileTypeControllerTest extends CRUDControllerTest<FileType> {

  @Override
  protected CRUDController<FileType> createController(Datastore<FileType> datastore) {
    return new FileTypeController(datastore);
  }

  @Override
  protected Supplier<String> getUniqueFieldName() {
    return () -> "type";
  }

  @Override
  protected FileType createNewObject(boolean withUUID) {
    return new FileType(
        !withUUID ? null : UUID.randomUUID(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString()
    );
  }

  @Override
  protected FileType setUniqueField(FileType object, String uniqueField) {
    return new FileType(
        object.uuid(),
        uniqueField,
        object.comment()
    );
  }
}

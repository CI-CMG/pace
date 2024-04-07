package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.data.FileType;
import java.util.UUID;
import java.util.function.Supplier;

class FileTypeControllerTest extends CRUDControllerTest<FileType, String> {

  @Override
  protected CRUDController<FileType, String> createController(Datastore<FileType, String> datastore) {
    return new FileTypeController(datastore);
  }

  @Override
  protected UniqueFieldProvider<FileType, String> getUniqueFieldProvider() {
    return FileType::getType;
  }

  @Override
  protected UUIDProvider<FileType> getUuidProvider() {
    return FileType::getUUID;
  }

  @Override
  protected UniqueFieldSetter<FileType, String> getUniqueFieldSetter() {
    return FileType::setType;
  }

  @Override
  protected Supplier<String> getUniqueFieldName() {
    return () -> "type";
  }

  @Override
  protected FileType createNewObject(boolean withUUID) {
    FileType fileType = new FileType();
    if (withUUID) {
      fileType.setUUID(UUID.randomUUID());
    }
    fileType.setUse(true);
    fileType.setType(UUID.randomUUID().toString());
    fileType.setComment(UUID.randomUUID().toString());
    return fileType;
  }
}

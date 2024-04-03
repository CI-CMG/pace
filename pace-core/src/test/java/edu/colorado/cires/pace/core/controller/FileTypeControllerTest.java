package edu.colorado.cires.pace.core.controller;

import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.repository.UUIDProvider;
import edu.colorado.cires.pace.core.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.core.service.CRUDService;
import edu.colorado.cires.pace.data.FileType;
import java.util.UUID;

class FileTypeControllerTest extends CRUDControllerTest<FileType, String> {

  @Override
  protected CRUDController<FileType, String> createController(CRUDService<FileType, String> service, Validator<FileType> validator) {
    return new FileTypeController(service, validator);
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
  protected FileType createNewObject() {
    FileType fileType = new FileType();
    fileType.setUUID(UUID.randomUUID());
    fileType.setUse(true);
    fileType.setType(UUID.randomUUID().toString());
    fileType.setComment(UUID.randomUUID().toString());
    return fileType;
  }
}

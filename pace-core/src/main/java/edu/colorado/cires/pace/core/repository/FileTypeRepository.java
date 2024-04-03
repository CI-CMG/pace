package edu.colorado.cires.pace.core.repository;

import edu.colorado.cires.pace.data.FileType;

public abstract class FileTypeRepository extends CRUDRepository<FileType, String> {

  protected FileTypeRepository() {
    super(FileType::getUUID, FileType::getType, FileType::setUUID);
  }

  @Override
  protected String getObjectName() {
    return FileType.class.getSimpleName();
  }

  @Override
  protected String getUniqueFieldName() {
    return "type";
  }
}

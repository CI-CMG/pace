package edu.colorado.cires.pace.core.repository;

import edu.colorado.cires.pace.core.datastore.Datastore;
import edu.colorado.cires.pace.data.FileType;

public class FileTypeRepository extends CRUDRepository<FileType, String> {

  protected FileTypeRepository(Datastore<FileType, String> datastore) {
    super(FileType::getUUID, FileType::getType, FileType::setUUID, datastore);
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

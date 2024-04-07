package edu.colorado.cires.pace.core.state.repository;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.FileType;

public class FileTypeRepository extends CRUDRepository<FileType, String> {

  public FileTypeRepository(Datastore<FileType, String> datastore) {
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

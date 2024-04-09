package edu.colorado.cires.pace.core.state.repository;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.FileType;

public class FileTypeRepository extends CRUDRepository<FileType> {

  public FileTypeRepository(Datastore<FileType> datastore) {
    super(datastore);
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

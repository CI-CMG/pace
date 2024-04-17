package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.datastore.Datastore;
import java.util.UUID;

public class FileTypeRepository extends CRUDRepository<FileType> {

  public FileTypeRepository(Datastore<FileType> datastore) {
    super(datastore);
  }

  @Override
  protected FileType setUUID(FileType object, UUID uuid) {
    return object.toBuilder()
        .uuid(uuid)
        .build();
  }

}

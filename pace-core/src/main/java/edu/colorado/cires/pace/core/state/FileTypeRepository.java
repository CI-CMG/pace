package edu.colorado.cires.pace.core.state;

import edu.colorado.cires.pace.data.object.FileType;
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

package edu.colorado.cires.pace.core.state;

import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.util.UUID;

public class FileTypeRepository extends CRUDRepository<FileType> {

  public FileTypeRepository(Datastore<FileType> datastore) {
    super(datastore);
  }

  @Override
  protected FileType setUUID(FileType object, UUID uuid) throws ValidationException {
    return object.toBuilder()
        .uuid(uuid)
        .build();
  }

}

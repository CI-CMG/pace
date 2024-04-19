package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.object.Instrument;
import edu.colorado.cires.pace.datastore.Datastore;
import edu.colorado.cires.pace.datastore.DatastoreException;
import java.util.UUID;

public class InstrumentRepository extends CRUDRepository<Instrument> {
  
  private final FileTypeRepository fileTypeRepository;

  public InstrumentRepository(Datastore<Instrument> datastore, Datastore<FileType> fileTypeDatastore) {
    super(datastore);
    this.fileTypeRepository = new FileTypeRepository(fileTypeDatastore);
  }

  @Override
  protected Instrument setUUID(Instrument object, UUID uuid) {
    return object.toBuilder()
        .uuid(uuid)
        .build();
  }

  @Override
  public Instrument create(Instrument object) throws DatastoreException, ConflictException, NotFoundException, BadArgumentException {
    checkFileTypes(object);
    return super.create(object);
  }

  @Override
  public Instrument update(UUID uuid, Instrument object) throws DatastoreException, ConflictException, NotFoundException, BadArgumentException {
    checkFileTypes(object);
    return super.update(uuid, object);
  }

  private void checkFileTypes(Instrument instrument) throws DatastoreException, BadArgumentException {
    for (FileType fileType : instrument.getFileTypes()) {
      try {
        fileTypeRepository.getByUUID(fileType.getUuid());
      } catch (NotFoundException e) {
        throw new BadArgumentException(String.format(
            "File type does not exist: %s", fileType.getType()
        ));
      }
    }
  }
}

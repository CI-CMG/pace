package edu.colorado.cires.pace.core.state.repository;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.core.exception.NotFoundException;
import edu.colorado.cires.pace.data.FileType;
import edu.colorado.cires.pace.data.Instrument;
import java.util.UUID;

public class InstrumentRepository extends CRUDRepository<Instrument> {
  
  private final FileTypeRepository fileTypeRepository;

  public InstrumentRepository(Datastore<Instrument> datastore, Datastore<FileType> fileTypeDatastore) {
    super(datastore);
    this.fileTypeRepository = new FileTypeRepository(fileTypeDatastore);
  }

  @Override
  public Instrument create(Instrument object) throws Exception {
    checkFileTypes(object);
    return super.create(object);
  }

  @Override
  public Instrument update(UUID uuid, Instrument object) throws Exception {
    checkFileTypes(object);
    return super.update(uuid, object);
  }

  @Override
  protected String getObjectName() {
    return Instrument.class.getSimpleName();
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }
  
  private void checkFileTypes(Instrument instrument) throws Exception {
    for (FileType fileType : instrument.fileTypes()) {
      try {
        fileTypeRepository.getByUUID(fileType.uuid());
      } catch (NotFoundException e) {
        throw new IllegalArgumentException(String.format(
            "File type does not exist: %s", fileType.type()
        ), e);
      }
    }
  }
}

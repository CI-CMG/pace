package edu.colorado.cires.pace.core.repository;

import edu.colorado.cires.pace.core.datastore.Datastore;
import edu.colorado.cires.pace.core.exception.NotFoundException;
import edu.colorado.cires.pace.data.FileType;
import edu.colorado.cires.pace.data.Instrument;
import java.util.UUID;

public class InstrumentRepository extends CRUDRepository<Instrument, String> {
  
  private final FileTypeRepository fileTypeRepository;

  public InstrumentRepository(Datastore<Instrument, String> datastore, FileTypeRepository fileTypeRepository) {
    super(Instrument::getUUID, Instrument::getName, Instrument::setUUID, datastore);
    this.fileTypeRepository = fileTypeRepository;
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
    for (FileType fileType : instrument.getFileTypes()) {
      try {
        fileTypeRepository.getByUUID(fileType.getUUID());
      } catch (NotFoundException e) {
        throw new IllegalArgumentException(String.format(
            "File type does not exist: %s", fileType.getType()
        ), e);
      }
    }
  }
}

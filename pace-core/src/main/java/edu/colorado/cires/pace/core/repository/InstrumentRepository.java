package edu.colorado.cires.pace.core.repository;

import edu.colorado.cires.pace.data.FileType;
import edu.colorado.cires.pace.data.Instrument;
import java.util.Optional;

public abstract class InstrumentRepository extends CRUDRepository<Instrument, String> {
  
  private final FileTypeRepository fileTypeRepository;

  protected InstrumentRepository(FileTypeRepository fileTypeRepository) {
    super(Instrument::getUUID, Instrument::getName, Instrument::setUUID);
    this.fileTypeRepository = fileTypeRepository;
  }

  @Override
  protected Instrument save(Instrument object) throws IllegalArgumentException {
    for (FileType fileType : object.getFileTypes()) {
      Optional<FileType> maybeFileType = fileTypeRepository.findByUUID(fileType.getUUID());
      if (maybeFileType.isEmpty()) {
        throw new IllegalArgumentException(String.format(
            "File type does not exist: %s", fileType.getType()
        ));
      }
    }
    return saveInstrument(object);
  }

  @Override
  protected String getObjectName() {
    return Instrument.class.getSimpleName();
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }
  
  protected abstract Instrument saveInstrument(Instrument instrument);
}

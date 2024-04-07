package edu.colorado.cires.pace.core.state.service;

import edu.colorado.cires.pace.core.state.repository.CRUDRepository;
import edu.colorado.cires.pace.data.FileType;

public class FileTypeService extends CRUDService<FileType, String> {

  public FileTypeService(CRUDRepository<FileType, String> fileTypeRepository) {
    super(fileTypeRepository);
  }
}

package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.fileType.FileType;
import edu.colorado.cires.pace.data.object.instrument.Instrument;
import edu.colorado.cires.pace.datastore.Datastore;

/**
 * FileTypeRepository extends UpstreamDependencyRepository and holds specifically
 * file type objects
 */
public class FileTypeRepository extends UpstreamDependencyRepository<FileType, Instrument> {

  @Override
  protected boolean dependencyAppliesToObject(Instrument dependency, FileType object) {
    return dependency.getFileTypes().contains(object.getType());
  }

  @Override
  protected String getDependentObjectUniqueFieldName() {
    return "name";
  }

  @Override
  protected Class<Instrument> getDependentObjectClass() {
    return Instrument.class;
  }

  @Override
  protected Instrument applyObjectToDependentObjects(FileType original, FileType updated, Instrument dependency) {
    return dependency.toBuilder()
        .fileTypes(replaceStringInList(dependency.getFileTypes(), original.getType(), updated.getType()))
        .build();
  }

  /**
   * Creates a file type repository
   * @param datastore holds file type objects
   * @param instrumentDatastore holds instrument type objects
   */
  public FileTypeRepository(Datastore<FileType> datastore, Datastore<Instrument> instrumentDatastore) {
    super(datastore, false, instrumentDatastore);
  }

}

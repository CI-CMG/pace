package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.object.Instrument;
import edu.colorado.cires.pace.datastore.Datastore;
import java.util.UUID;

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

  public FileTypeRepository(Datastore<FileType> datastore, Datastore<Instrument> instrumentDatastore) {
    super(datastore, false, instrumentDatastore);
  }

}

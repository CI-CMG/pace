package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.object.Instrument;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.datastore.Datastore;
import edu.colorado.cires.pace.datastore.DatastoreException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class InstrumentRepository extends PackageDependencyRepository<Instrument> implements DownstreamDependencyRepository<Instrument> {
  
  private final Datastore<FileType> fileTypeDatastore;

  public InstrumentRepository(Datastore<Instrument> datastore, Datastore<FileType> fileTypeDatastore, Datastore<Package> packageDatastore) {
    super(datastore, packageDatastore);
    this.fileTypeDatastore = fileTypeDatastore;
  }

  @Override
  public Instrument create(Instrument object) throws DatastoreException, ConflictException, NotFoundException, BadArgumentException {
    checkDownstreamDependencies(object);
    return super.create(object);
  }

  @Override
  protected boolean dependencyAppliesToObject(Package dependency, Instrument object) {
    return object.getName().equals(dependency.getInstrument());
  }

  @Override
  protected Package applyObjectToDependentObjects(Instrument original, Instrument updated, Package dependency) {
    return dependency.setInstrument(
        replaceString(
            dependency.getInstrument(), original.getName(), updated.getName()
        )
    );
  }

  @Override
  public Instrument update(UUID uuid, Instrument object) throws DatastoreException, ConflictException, NotFoundException, BadArgumentException {
    checkDownstreamDependencies(object);
    return super.update(uuid, object);
  }

  @Override
  public void checkDownstreamDependencies(Instrument object) throws DatastoreException, ConstraintViolationException {
    Set<ConstraintViolation<Instrument>> constraintViolations = new HashSet<>(0);
    List<String> fileTypes = object.getFileTypes();
    for (int i = 0; i < fileTypes.size(); i++) {
      String fileType = fileTypes.get(i);
      Optional<FileType> maybeFileType = fileTypeDatastore.findByUniqueField(fileType);
      if (maybeFileType.isEmpty()) {
        constraintViolations.add(ConstraintViolationFactory.create(
            String.format(
                "%s with %s = %s does not exist", fileTypeDatastore.getClassName(), fileTypeDatastore.getUniqueFieldName(), fileType
            ),
            String.format("fileTypes[%s]", i)
        ));
      }
    }
    
    if (!constraintViolations.isEmpty()) {
      throw new ConstraintViolationException(String.format(
          "%s validation failed", getClassName()
      ), constraintViolations);
    }
  }
}

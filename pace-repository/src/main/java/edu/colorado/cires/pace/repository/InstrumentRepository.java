package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.fileType.FileType;
import edu.colorado.cires.pace.data.object.instrument.Instrument;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.datastore.Datastore;
import edu.colorado.cires.pace.datastore.DatastoreException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * InstrumentRepository extends PackageDependencyRepository and holds specifically
 * instrument objects
 */
public class InstrumentRepository extends PackageDependencyRepository<Instrument> implements DownstreamDependencyRepository<Instrument> {
  
  private final Datastore<FileType> fileTypeDatastore;

  /**
   * Creates an instrument repository
   * @param datastore holds instrument objects
   * @param fileTypeDatastore holds file type objects
   * @param packageDatastore holds package objects
   */
  public InstrumentRepository(Datastore<Instrument> datastore, Datastore<FileType> fileTypeDatastore, Datastore<Package> packageDatastore) {
    super(datastore, packageDatastore);
    this.fileTypeDatastore = fileTypeDatastore;
  }

  /**
   * Saves an instrument object to the repository
   * @param object object to be saved to the repository
   * @return Instrument provided object after any updates
   * @throws DatastoreException thrown in case of error interacting with datastore
   * @throws ConflictException thrown in case of conflicting uuids in datastore
   * @throws NotFoundException thrown in case of object not found
   * @throws BadArgumentException thrown in case of bad argument
   */
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
    return dependency.toBuilder().instrument(
        replaceString(
            dependency.getInstrument(), original.getName(), updated.getName()
        )
    ).build();
  }

  /**
   * Updates an instrument in the repository with the provided uuid
   * @param uuid uuid to identify object to update by
   * @param object updated object
   * @return Instrument provided object after any updates
   * @throws DatastoreException thrown in case of error interacting with datastore
   * @throws ConflictException thrown in case of conflicting uuids in datastore
   * @throws NotFoundException thrown in case of object not found
   * @throws BadArgumentException thrown in case of bad argument
   */
  @Override
  public Instrument update(UUID uuid, Instrument object) throws DatastoreException, ConflictException, NotFoundException, BadArgumentException {
    checkDownstreamDependencies(object);
    return super.update(uuid, object);
  }

  /**
   * Ensures that the relevant file type exists for the instrument object
   * @param object instrument object to check dependencies for
   * @throws DatastoreException thrown in case of error interacting with datastore
   * @throws ConstraintViolationException thrown in case of downstream dependencies not being locatable
   */
  @Override
  public void checkDownstreamDependencies(Instrument object) throws DatastoreException, ConstraintViolationException {
    validate(object);
    
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

package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.datastore.Datastore;
import edu.colorado.cires.pace.datastore.DatastoreException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JSON datastores hold all json objects in pace
 * @param <O>
 */
abstract class JsonDatastore<O extends AbstractObject> implements Datastore<O> {
  
  private final Logger LOGGER;
  
  private final Path storageDirectory;
  private final ObjectMapper objectMapper;
  private final Class<O> clazz;
  private final Function<O, String> uniqueFieldGetter;
  private final ConcurrentHashMap<String, O> objectsMap = new ConcurrentHashMap<>(0);

  private boolean initialized = false;

  protected JsonDatastore(Path storageDirectory, ObjectMapper objectMapper, Class<O> clazz, Function<O, String> uniqueFieldGetter) throws IOException {
    this.LOGGER = LoggerFactory.getLogger(this.getClass());
    this.storageDirectory = storageDirectory;
    this.objectMapper = objectMapper;
    this.clazz = clazz;
    this.uniqueFieldGetter = uniqueFieldGetter;
  }
  
  private void init() throws IOException {
    if (!storageDirectory.toFile().exists()) {
      LOGGER.debug("Creating {}", storageDirectory);
      Path parentPath = storageDirectory.getParent();
      if (parentPath != null && !parentPath.toFile().exists()) {
        Files.createDirectories(parentPath);
      }
      Files.createDirectories(storageDirectory);
    }

    readStorageFiles();
    
    initialized = true;
  }

  /**
   * Save writes the provided object to the datastore, updating the object
   * if it already exists within the datastore
   *
   * @param object to be written to datastore
   * @return O object which was provided
   * @throws DatastoreException in case where save fails
   */
  @Override
  public O save(O object) throws DatastoreException {
    lazyInitialize();
    String uniqueField = uniqueFieldGetter.apply(object);
    
    List<O> existingObjects = objectsMap.values().stream()
        .filter(o -> o.getUuid().equals(object.getUuid()))
        .toList();
    
    try {
      if (!existingObjects.isEmpty()) { // update
        existingObjects.forEach(o -> objectsMap.remove(uniqueFieldGetter.apply(o)));
      }
      
      objectsMap.put(uniqueField, object);
      writeStorageFile(object);
      LOGGER.debug("Wrote {} with {} = {}, uuid = {} to {}", getClassName(), getUniqueFieldName(), uniqueField, object.getUuid(), storageDirectory);
      return object;
    } catch (Exception e) {
      throw new DatastoreException(String.format(
          "%s save failed", object.getUuid()
      ), e);
    }
  }

  /**
   * Delete removes the provided object from the datastore
   *
   * @param object to be deleted from datastore
   * @throws DatastoreException in case where deletion fails
   */
  @Override
  public void delete(O object) throws DatastoreException {
    lazyInitialize();
    String uniqueField = uniqueFieldGetter.apply(object);
    try {
      objectsMap.remove(uniqueField);
      LOGGER.debug("Removed {} with {} = {} from objects", getClassName(), getUniqueFieldName(), uniqueField);
      deleteStorageFile(object);
    } catch (IOException e) {
      throw new DatastoreException(String.format(
          "%s delete failed", object.getUuid()
      ), e);
    }
  }

  /**
   * Deletes file from storage by finding file name based on object
   *
   * @param object to delete from storage
   * @throws IOException in case of input or output error
   */
  private void deleteStorageFile(O object) throws IOException {
    Files.delete(storageDirectory.resolve(fileNameFromObject(object)));
  }

  /**
   * Finds the file name of object based on uuid
   *
   * @param object to find the file name of
   * @return String file name
   */
  private String fileNameFromObject(O object) {
    return String.format(
        "%s.json", object.getUuid()
    );
  }

  /**
   * Given uuid, findByUUID returns the relevant object
   *
   * @param uuid of object
   * @return object which uuid corresponds to
   * @throws DatastoreException in case of search error
   */
  @Override
  public Optional<O> findByUUID(UUID uuid) throws DatastoreException {
    lazyInitialize();
    return objectsMap.values().stream()
        .filter(o -> o.getUuid().equals(uuid))
        .findFirst();
  }

  /**
   * Given a unique field, findByUniqueField returns the relevant object
   *
   * @param uniqueField of object
   * @return object which unique field corresponds to
   * @throws DatastoreException in case of search error
   */
  @Override
  public Optional<O> findByUniqueField(String uniqueField) throws DatastoreException {
    lazyInitialize();
    return Optional.ofNullable(
        objectsMap.get(uniqueField)
    );
  }

  /**
   * Returns a stream of all objects in the datastore
   *
   * @return Stream of objects in the datastore
   * @throws DatastoreException in case of search error
   */
  @Override
  public Stream<O> findAll() throws DatastoreException {
    lazyInitialize();
    return objectsMap.values().stream().sorted(Comparator.comparing(AbstractObject::getUniqueField));
  }

  private void readStorageFiles() throws IOException {
    LOGGER.debug("Reading all {} objects from {}", getClassName(), storageDirectory);
    Files.walk(storageDirectory)
        .map(Path::toFile)
        .filter(File::isFile)
        .forEach(
            file -> {
              try {
                O object = objectMapper.readValue(file, clazz);
                objectsMap.put(object.getUniqueField(), object);
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            }
        );
  }
  
  private void writeStorageFile(O object) throws IOException {
    LOGGER.debug("Writing {} objects to {}", getClassName(), storageDirectory);
    objectMapper.writerFor(clazz).writeValue(
        storageDirectory.resolve(fileNameFromObject(object)).toFile(),
        object
    );
  }

  /**
   * Returns simplified class name
   * @return String of class simple name
   */
  @Override
  public String getClassName() {
    return clazz.getSimpleName();
  }

  /**
   * Returns a getter function for unique fields
   * @return Function of O and String to find unique field
   */
  public Function<O, String> getUniqueFieldGetter() {
    return uniqueFieldGetter;
  }
  
  private void lazyInitialize() throws DatastoreException {
    if (!initialized) {
      try {
        init();
      } catch (IOException e) {
        throw new DatastoreException("Datastore initialization failed", e);
      }
    }
  }
}

package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.Datastore;
import edu.colorado.cires.pace.datastore.DatastoreException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

abstract class JsonDatastore<O extends ObjectWithUniqueField> implements Datastore<O> {
  
  private final Path storageFile;
  private final ObjectMapper objectMapper;
  private final Class<O> clazz;
  private final Function<O, String> uniqueFieldGetter;
  private final Map<String, O> objectsMap = new HashMap<>(0);
  private final TypeReference<List<O>> typeReference;

  protected JsonDatastore(Path storageFile, ObjectMapper objectMapper, Class<O> clazz, Function<O, String> uniqueFieldGetter,
      TypeReference<List<O>> typeReference) throws IOException {
    this.storageFile = storageFile;
    this.objectMapper = objectMapper;
    this.clazz = clazz;
    this.uniqueFieldGetter = uniqueFieldGetter;
    this.typeReference = typeReference;
    init();
  }
  
  private void init() throws IOException {
    if (!storageFile.toFile().exists()) {
      Files.createDirectories(storageFile.getParent());
      objectMapper.writeValue(storageFile.toFile(), Collections.emptyList());
    }
    
    readStorageFile();
  }

  @Override
  public O save(O object) throws DatastoreException {
    String uniqueField = uniqueFieldGetter.apply(object);
    
    List<O> existingObjects = objectsMap.values().stream()
        .filter(o -> o.getUuid().equals(object.getUuid()))
        .toList();
    
    try {
      if (!existingObjects.isEmpty()) { // update
        existingObjects.forEach(o -> objectsMap.remove(uniqueFieldGetter.apply(o)));
      }
      
      objectsMap.put(uniqueField, object);
      writeStorageFile();
      return object;
    } catch (Exception e) {
      throw new DatastoreException(String.format(
          "%s save failed", object.getUuid()
      ), e);
    }
  }

  @Override
  public void delete(O object) throws DatastoreException {
    String uniqueField = uniqueFieldGetter.apply(object);
    try {
      objectsMap.remove(uniqueField);
      writeStorageFile();
    } catch (IOException e) {
      throw new DatastoreException(String.format(
          "%s delete failed", object.getUuid()
      ), e);
    }
  }

  @Override
  public Optional<O> findByUUID(UUID uuid) {
    return objectsMap.values().stream()
        .filter(o -> o.getUuid().equals(uuid))
        .findFirst();
  }

  @Override
  public Optional<O> findByUniqueField(String uniqueField) {
    return Optional.ofNullable(
        objectsMap.get(uniqueField)
    );
  }

  @Override
  public Stream<O> findAll() {
    return objectsMap.values().stream();
  }

  private void readStorageFile() throws IOException {
    List<O> objects = objectMapper.readValue(storageFile.toFile(), typeReference);
    objects.forEach(o -> objectsMap.put(
        uniqueFieldGetter.apply(o), o
    ));
  }
  
  private void writeStorageFile() throws IOException {
    objectMapper.writerFor(typeReference).writeValue(
        storageFile.toFile(),
        objectsMap.values().stream().toList()
    );
    readStorageFile();
  }

  @Override
  public String getClassName() {
    return clazz.getSimpleName();
  }

  public Function<O, String> getUniqueFieldGetter() {
    return uniqueFieldGetter;
  }
}

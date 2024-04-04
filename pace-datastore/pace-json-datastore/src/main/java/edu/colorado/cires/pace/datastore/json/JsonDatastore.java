package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.datastore.Datastore;
import edu.colorado.cires.pace.core.repository.UUIDProvider;
import edu.colorado.cires.pace.core.repository.UniqueFieldProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class JsonDatastore<O, U> implements Datastore<O, U> {
  
  private final Path storageDirectory;
  private final ObjectMapper objectMapper;
  private final Class<O> clazz;
  private final UUIDProvider<O> uuidProvider;
  private final UniqueFieldProvider<O, U> uniqueFieldProvider;

  protected JsonDatastore(Path storageDirectory, ObjectMapper objectMapper, Class<O> clazz, UUIDProvider<O> uuidProvider,
      UniqueFieldProvider<O, U> uniqueFieldProvider) throws IOException {
    this.storageDirectory = storageDirectory;
    this.objectMapper = objectMapper;
    this.clazz = clazz;
    this.uuidProvider = uuidProvider;
    this.uniqueFieldProvider = uniqueFieldProvider;
    init();
  }
  
  private void init() throws IOException {
    if (!storageDirectory.toFile().exists()) {
      Files.createDirectories(storageDirectory);
    }
  }

  @Override
  public O save(O object) throws IOException {
    String fileName = getFileName(object);
    try (OutputStream outputStream = new FileOutputStream(storageDirectory.resolve(fileName).toFile())) {
      objectMapper.writeValue(outputStream, object);
      return object;
    }
  }

  @Override
  public void delete(O object) throws IOException {
    String fileName = getFileName(object);
    Files.delete(storageDirectory.resolve(fileName));
  }

  @Override
  public Optional<O> findByUUID(UUID uuid) throws IOException {
    return getObjectStream(
        (o) -> uuidProvider.getUUID(o).equals(uuid)
    ).findFirst();
  }

  @Override
  public Optional<O> findByUniqueField(U uniqueField) throws IOException {
    return getObjectStream(
        (o) -> uniqueFieldProvider.getUniqueField(o).equals(uniqueField)
    ).findFirst();
  }

  @Override
  public Stream<O> findAll() throws IOException {
    return getObjectStream((o) -> true);
  }
  
  private Stream<O> getObjectStream(Function<O, Boolean> filter) throws IOException {
    return Files.list(storageDirectory)
        .map(Path::toFile)
        .filter(File::isFile)
        .map(f -> {
          try (InputStream inputStream = new FileInputStream(f)) {
            return objectMapper.readValue(inputStream, clazz);
          } catch (IOException e) {
            throw new IllegalStateException(String.format(
                "Failed to deserialize %s", f
            ), e);
          }
        }).filter(filter::apply);
  }
  
  private String getFileName(O object) {
    return String.format(
        "%s.json", uuidProvider.getUUID(object)
    );
  }
}
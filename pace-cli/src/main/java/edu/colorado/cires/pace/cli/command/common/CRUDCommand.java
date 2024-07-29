package edu.colorado.cires.pace.cli.command.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.utilities.ApplicationPropertyResolver;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

abstract class CRUDCommand<O extends AbstractObject> implements Runnable {
  protected final ObjectMapper objectMapper = SerializationUtils.createObjectMapper();
  protected abstract RepositoryFactory<O> getRepositoryFactory(); 
  
  protected CRUDRepository<O> createRepository() throws IOException {
    return getRepositoryFactory().createRepository(
        getDatastoreDirectory(),
        objectMapper
    );
  }

  protected abstract Object runCommand()
      throws Exception;

  private Path getDatastoreDirectory() {
    return ApplicationPropertyResolver.getDataDir();
  }
  
  private String writeObject(Object object) throws JsonProcessingException {
    Class<?> clazz = object.getClass();
    ObjectWriter objectWriter;
    if (object instanceof List<?> objects) {
      if (objects.isEmpty()) {
        objectWriter = objectMapper.writer();
      } else {
        clazz = objects.get(0).getClass();
        objectWriter = objectMapper.writerFor(
            objectMapper.getTypeFactory().constructCollectionType(List.class, clazz)
        );
      }
    } else {
      objectWriter = objectMapper.writerFor(clazz);
    }
    return objectWriter.withDefaultPrettyPrinter().writeValueAsString(
        object
    );
  }
  
  @Override
  public void run() {
    try {
      Object object = runCommand();
      if (object != null) {
        System.out.println(
            writeObject(object)
        );
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

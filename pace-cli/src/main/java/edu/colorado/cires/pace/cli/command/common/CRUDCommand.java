package edu.colorado.cires.pace.cli.command.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.utilities.ApplicationPropertyResolver;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

abstract class CRUDCommand<O extends ObjectWithUniqueField> implements Runnable {
  protected final ObjectMapper objectMapper = SerializationUtils.createObjectMapper();
  protected abstract RepositoryFactory<O> getRepositoryFactory(); 
  
  protected CRUDRepository<O> createRepository() throws IOException {
    return getRepositoryFactory().createRepository(
        getDatastoreDirectory(),
        objectMapper
    );
  }

  protected abstract Object runCommand()
      throws IOException, DatastoreException, NotFoundException, ConflictException, BadArgumentException, BatchWriteException;

  private Path getDatastoreDirectory() {
    return new ApplicationPropertyResolver().getWorkDir();
  }
  
  private String writeObject(Object object) throws JsonProcessingException {
    Class<O> clazz = getClazz();
    ObjectWriter objectWriter;
    if (object instanceof List<?>) {
      objectWriter = objectMapper.writerFor(
          objectMapper.getTypeFactory().constructCollectionType(List.class, clazz)
      );
    } else {
      objectWriter = objectMapper.writerFor(clazz);
    }
    return objectWriter.withDefaultPrettyPrinter().writeValueAsString(
        object
    );
  }
  
  protected abstract Class<O> getClazz();
  
  @Override
  public void run() {
    try {
      System.out.println(
          writeObject(runCommand())
      );
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    } catch (ConflictException e) {
      throw new RuntimeException(e);
    } catch (NotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (DatastoreException e) {
      throw new RuntimeException(e);
    } catch (BadArgumentException e) {
      throw new RuntimeException(e);
    } catch (BatchWriteException e) {
      throw new RuntimeException(e);
    }
  }
}

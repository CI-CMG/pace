package edu.colorado.cires.pace.cli.command.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.util.ApplicationPropertyResolver;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

abstract class CRUDCommand<O extends ObjectWithUniqueField> implements Runnable {
  protected final ObjectMapper objectMapper = new ObjectMapper();
  protected abstract RepositoryFactory<O> getRepositoryFactory(); 
  
  protected CRUDRepository<O> createRepository() throws IOException {
    return getRepositoryFactory().createRepository(
        getDatastoreDirectory(),
        objectMapper
    );
  }

  protected abstract Object runCommand() throws IOException, DatastoreException, NotFoundException, ConflictException, BadArgumentException;

  private Path getDatastoreDirectory() {
    return new ApplicationPropertyResolver().getWorkDir();
  }
  
  @Override
  public void run() {
    try {
      System.out.println(
          objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
              runCommand()
          )
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
    }
  }
}

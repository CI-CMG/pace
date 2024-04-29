package edu.colorado.cires.pace.cli.command.common;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.util.SerializationUtils;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

abstract class JsonBlobCommand<O extends ObjectWithUniqueField> extends CRUDCommand<O> {
  
  protected abstract Supplier<File> getJsonBlobProvider();
  protected abstract Class<O> getJsonClass();
  public abstract TypeReference<List<O>> getTypeReference();

  @Override
  protected Object runCommand()
      throws IOException, ConflictException, NotFoundException, DatastoreException, BadArgumentException, BatchWriteException {
    try {
      return SerializationUtils.deserializeAndProcess(
          objectMapper,
          getJsonBlobProvider().get(),
          getJsonClass(),
          getTypeReference(),
          (o) -> {
            try {
              return runCommandWithDeserializedObject(o);
            } catch (IOException | ConflictException | NotFoundException | DatastoreException | BadArgumentException e) {
              throw new RuntimeException(e);
            }
          }
      );
    } catch (RuntimeException e) {
      Throwable throwable = e.getCause();
      
      if (throwable instanceof ConflictException) {
        throw (ConflictException) throwable;
      }

      if (throwable instanceof NotFoundException) {
        throw (NotFoundException) throwable;
      }

      if (throwable instanceof DatastoreException) {
        throw (DatastoreException) throwable;
      }

      if (throwable instanceof BadArgumentException) {
        throw (BadArgumentException) throwable;
      }

      if (throwable instanceof BatchWriteException) {
        throw (BatchWriteException) throwable;
      }
      
      throw new IllegalStateException(e);
    }
  }
  
  protected abstract O runCommandWithDeserializedObject(O object)
      throws IOException, ConflictException, NotFoundException, DatastoreException, BadArgumentException;
}

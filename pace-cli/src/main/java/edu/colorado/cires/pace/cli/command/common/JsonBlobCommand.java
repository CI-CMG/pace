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
  protected abstract Supplier<Boolean> isMultivalued();
  public abstract TypeReference<List<O>> getTypeReference();

  @Override
  protected Object runCommand()
      throws IOException, ConflictException, NotFoundException, DatastoreException, BadArgumentException, BatchWriteException {
    if (isMultivalued().get()) {
      List<O> objects = SerializationUtils.deserializeBlobs(objectMapper, getJsonBlobProvider().get(), getTypeReference());
      List<O> resultsObjects = new ArrayList<>(objects.size());
      
      RuntimeException runtimeException = new RuntimeException();
      for (O object : objects) {
        try {
          resultsObjects.add(
              runCommandWithDeserializedObject(object)
          );
        } catch (Exception e) {
          runtimeException.addSuppressed(e);
        }
      }
      
      if (runtimeException.getSuppressed().length != 0) {
        BatchWriteException exception = new BatchWriteException("Batch command failed");

        for (Throwable throwable : runtimeException.getSuppressed()) {
          exception.addSuppressed(throwable);
        }
        
        throw exception;
      }
      
      return resultsObjects;
    } else {
      return runCommandWithDeserializedObject(
          SerializationUtils.deserializeBlob(objectMapper, getJsonBlobProvider().get(), getJsonClass())
      );
    }
  }
  
  protected abstract O runCommandWithDeserializedObject(O object)
      throws IOException, ConflictException, NotFoundException, DatastoreException, BadArgumentException;
}

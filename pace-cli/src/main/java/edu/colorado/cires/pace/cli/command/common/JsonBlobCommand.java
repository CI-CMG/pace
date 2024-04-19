package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.cli.util.SerializationUtils;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

abstract class JsonBlobCommand<O extends ObjectWithUniqueField> extends CRUDCommand<O> {
  
  protected abstract Supplier<File> getJsonBlobProvider();
  protected abstract Class<O> getJsonClass();

  @Override
  protected Object runCommand() throws IOException, ConflictException, NotFoundException, DatastoreException, BadArgumentException {
    return runCommandWithDeserializedObject(
        SerializationUtils.deserializeBlob(objectMapper, getJsonBlobProvider().get(), getJsonClass())
    );
  }
  
  protected abstract O runCommandWithDeserializedObject(O object)
      throws IOException, ConflictException, NotFoundException, DatastoreException, BadArgumentException;
}

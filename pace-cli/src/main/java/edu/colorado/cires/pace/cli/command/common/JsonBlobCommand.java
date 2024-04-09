package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.cli.util.SerializationUtils;
import edu.colorado.cires.pace.data.ObjectWithUniqueField;
import java.io.File;
import java.util.function.Supplier;

public abstract class JsonBlobCommand<O extends ObjectWithUniqueField> extends CRUDCommand<O> {
  
  protected abstract Supplier<File> getJsonBlobProvider();
  protected abstract Class<O> getJsonClass();

  @Override
  protected Object runCommand() throws Exception {
    return runCommandWithDeserializedObject(
        SerializationUtils.deserializeBlob(objectMapper, getJsonBlobProvider().get(), getJsonClass())
    );
  }
  
  protected abstract O runCommandWithDeserializedObject(O object) throws Exception;
}

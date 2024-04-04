package edu.colorado.cires.pace.cli.command.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.function.Supplier;

public abstract class JsonBlobCommand<O, U> extends CRUDCommand<O, U> {
  
  protected abstract Supplier<String> getJsonBlobProvider();
  protected abstract Class<O> getJsonClass();

  @Override
  protected Object runCommand() throws Exception {
    return runCommandWithDeserializedObject(
        deserializeBlob()
    );
  }
  
  protected abstract O runCommandWithDeserializedObject(O object) throws Exception;
  
  private O deserializeBlob() throws JsonProcessingException {
    System.out.println(getJsonBlobProvider().get());
    return objectMapper.readValue(getJsonBlobProvider().get(), getJsonClass());
  }
}

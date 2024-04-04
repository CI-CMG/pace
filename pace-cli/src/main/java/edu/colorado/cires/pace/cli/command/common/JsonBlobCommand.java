package edu.colorado.cires.pace.cli.command.common;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public abstract class JsonBlobCommand<O, U> extends CRUDCommand<O, U> {
  
  protected abstract Supplier<File> getJsonBlobProvider();
  protected abstract Class<O> getJsonClass();

  @Override
  protected Object runCommand() throws Exception {
    return runCommandWithDeserializedObject(
        deserializeBlob()
    );
  }
  
  protected abstract O runCommandWithDeserializedObject(O object) throws Exception;
  
  private O deserializeBlob() throws IOException {
    return objectMapper.readValue(
        getJsonContents(), getJsonClass()
    );
  }
  
  private String getJsonContents() throws IOException {
    File file = getJsonBlobProvider().get();
    return "-".equals(file.getName())
        ? IOUtils.toString(System.in, StandardCharsets.UTF_8) : FileUtils.readFileToString(file, StandardCharsets.UTF_8);
  }
}

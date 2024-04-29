package edu.colorado.cires.pace.cli.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public final class SerializationUtils {
  
  public static ObjectMapper createObjectMapper() {
    DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
    prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
    
    return new ObjectMapper()
        .setSerializationInclusion(Include.NON_NULL)
        .registerModule(new JavaTimeModule())
        .setDefaultPrettyPrinter(prettyPrinter);
  }

  public static <T> T deserializeBlob(ObjectMapper objectMapper, File file, Class<T> clazz) throws IOException {
    return objectMapper.readValue(
        getJsonContents(file), clazz
    );
  }
  
  public static <T> List<T> deserializeBlobs(ObjectMapper objectMapper, File file, TypeReference<List<T>> typeReference) throws IOException {
    return objectMapper.readValue(
        getJsonContents(file),
        typeReference
    );
  }

  private static String getJsonContents(File file) throws IOException {
    return "-".equals(file.getName())
        ? IOUtils.toString(System.in, StandardCharsets.UTF_8) : FileUtils.readFileToString(file, StandardCharsets.UTF_8);
  }

}

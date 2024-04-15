package edu.colorado.cires.pace.cli.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public final class SerializationUtils {
  
  public static ObjectMapper createObjectMapper() {
    return new ObjectMapper()
        .setSerializationInclusion(Include.NON_NULL);
  }

  public static <T> T deserializeBlob(ObjectMapper objectMapper, File file, Class<T> clazz) throws IOException {
    return objectMapper.readValue(
        getJsonContents(file), clazz
    );
  }

  private static String getJsonContents(File file) throws IOException {
    return "-".equals(file.getName())
        ? IOUtils.toString(System.in, StandardCharsets.UTF_8) : FileUtils.readFileToString(file, StandardCharsets.UTF_8);
  }

}

package edu.colorado.cires.pace.utilities;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public final class SerializationUtils {

  public static ObjectMapper createObjectMapper() {
    DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
    prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);

    return new ObjectMapper()
        .setSerializationInclusion(Include.NON_NULL)
        .registerModule(new JavaTimeModule())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .setDefaultPrettyPrinter(prettyPrinter);
  }

  private static <T> T deserializeBlob(ObjectMapper objectMapper, String jsonContents, Class<T> clazz) throws IOException {
    return objectMapper.readValue(
        jsonContents, clazz
    );
  }

  private static <T> List<T> deserializeBlobs(ObjectMapper objectMapper, String jsonContents, TypeReference<List<T>> typeReference) throws IOException {
    return objectMapper.readValue(
        jsonContents,
        typeReference
    );
  }

  private static String getJsonContents(File file) throws IOException {
    return "-".equals(file.getName())
        ? IOUtils.toString(System.in, StandardCharsets.UTF_8) : FileUtils.readFileToString(file, StandardCharsets.UTF_8);
  }

  public static <T> Object deserializeAndProcess(ObjectMapper objectMapper, File file, Class<T> tClass, TypeReference<List<T>> typeReference, Function<T, T> processor) throws IOException {
    String jsonContents = getJsonContents(file);

    JsonNode jsonNode = objectMapper.readTree(
        jsonContents
    );

    if (jsonNode instanceof ArrayNode) {
      List<T> deserializedList = deserializeBlobs(objectMapper, jsonContents, typeReference);

      List<T> processedList = new ArrayList<>(deserializedList.size());
      for (T deserializedObject : deserializedList) {
        T processed = processor.apply(deserializedObject);
        if (processed != null) {
          processedList.add(processed);
        }
      }

      return processedList;
    } else if (jsonNode instanceof ObjectNode) {
      T deserializedObject = deserializeBlob(objectMapper, jsonContents, tClass);
      return processor.apply(deserializedObject);
    } else {
      throw new IOException(
          "Unknown input type. Expected list or object."
      );
    }
  }

}

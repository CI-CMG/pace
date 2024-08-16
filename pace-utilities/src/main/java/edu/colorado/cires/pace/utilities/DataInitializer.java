package edu.colorado.cires.pace.utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;

class DataInitializer {
  public static void initialize(Path outputPath, ObjectMapper objectMapper, String jsonName) throws URISyntaxException, IOException {
    Path jsonPath = outputPath.resolve(jsonName);
    if (!jsonPath.toFile().exists()) {
      if (!jsonPath.toFile().mkdirs()) {
        throw new IOException(String.format("Unable to create directory %s.", jsonPath.toAbsolutePath().toString()));
      }
    }
    try (InputStream inputStream = DataInitializer.class.getResourceAsStream("/"+jsonName+".json")) {
      JsonNode jsonNode = objectMapper.readTree(inputStream);
      if (jsonNode instanceof ArrayNode arrayNode) {
        for (JsonNode node : arrayNode) {
          String uuid = node.get("uuid").asText();
          objectMapper.writeValue(
              jsonPath.resolve(String.format("%s.json", uuid)).toFile(),
              node
          );
        }
      } else {
        throw new IllegalStateException("JSON object is not an array");
      }
    }
  }
}

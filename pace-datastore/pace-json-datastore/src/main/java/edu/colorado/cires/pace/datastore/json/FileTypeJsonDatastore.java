package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.FileType;
import java.io.IOException;
import java.nio.file.Path;

public class FileTypeJsonDatastore extends JsonDatastore<FileType> {

  public FileTypeJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("file-types.json"), objectMapper, FileType.class, FileType::getType, new TypeReference<>() {});
  }

  @Override
  public String getUniqueFieldName() {
    return "type";
  }
}

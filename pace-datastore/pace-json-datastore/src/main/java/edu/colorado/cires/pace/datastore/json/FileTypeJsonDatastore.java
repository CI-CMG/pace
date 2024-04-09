package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.FileType;
import java.io.IOException;
import java.nio.file.Path;

public class FileTypeJsonDatastore extends JsonDatastore<FileType> {

  public FileTypeJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("file-types"), objectMapper, FileType.class);
  }
}

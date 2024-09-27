package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.fileType.FileType;
import java.io.IOException;
import java.nio.file.Path;

/**
 * FileTypeJsonDatastore extends JsonDatastore and provides type as unique
 * field name
 */
public class FileTypeJsonDatastore extends JsonDatastore<FileType> {

  /**
   *
   *
   * @param workDirectory
   * @param objectMapper
   * @throws IOException
   */
  public FileTypeJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("file-types"), objectMapper, FileType.class, FileType::getType);
  }

  @Override
  public String getUniqueFieldName() {
    return "type";
  }
}

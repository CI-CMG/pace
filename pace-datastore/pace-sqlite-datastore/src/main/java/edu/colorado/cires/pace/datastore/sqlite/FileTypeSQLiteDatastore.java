package edu.colorado.cires.pace.datastore.sqlite;

import edu.colorado.cires.pace.data.object.fileType.FileType;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * FileTypeSQLiteDatastore extends SQLiteDatastore and provides an initializer
 */
public class FileTypeSQLiteDatastore extends SQLiteDatastore<FileType> {

  /**
   * Initializes a file type sqlite datastore
   * @param sqliteFile path to datastore
   */
  public FileTypeSQLiteDatastore(Path sqliteFile) {
    super(sqliteFile, "FILE_TYPES", FileType.class);
  }

  @Override
  protected Stream<FileType> resultSetToStream(ResultSet resultSet) throws SQLException {
    List<FileType> fileTypes = new ArrayList<>(0);
    
    while (resultSet.next()) {
      fileTypes.add(FileType.builder()
              .type(resultSet.getString("TYPE"))
          .build());
    }
    
    return fileTypes.stream();
  }
}

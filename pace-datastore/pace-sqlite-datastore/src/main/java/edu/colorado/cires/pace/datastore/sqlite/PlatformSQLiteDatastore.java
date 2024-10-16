package edu.colorado.cires.pace.datastore.sqlite;

import edu.colorado.cires.pace.data.object.platform.Platform;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * PlatformSQLiteDatastore extends SQLiteDatastore and provides an initializer
 */
public class PlatformSQLiteDatastore extends SQLiteDatastore<Platform> {

  /**
   * Initializes a platform type sqlite datastore
   * @param sqliteFile path to datastore
   */
  public PlatformSQLiteDatastore(Path sqliteFile) {
    super(sqliteFile, "PLATFORMS", Platform.class);
  }

  @Override
  protected Stream<Platform> resultSetToStream(ResultSet resultSet) throws SQLException {
    List<Platform> platforms = new ArrayList<>(0);
    
    while (resultSet.next()) {
      platforms.add(Platform.builder()
              .name(resultSet.getString("NAME"))
          .build());
    }
    
    return platforms.stream();
  }
}

package edu.colorado.cires.pace.datastore.sqlite;

import edu.colorado.cires.pace.data.object.sea.Sea;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * SeaSQLiteDatastore extends SQLiteDatastore and provides an initializer
 */
public class SeaSQLiteDatastore extends SQLiteDatastore<Sea> {

  /**
   * Initializes a sea type sqlite datastore
   * @param sqliteFile path to datastore
   */
  public SeaSQLiteDatastore(Path sqliteFile) {
    super(sqliteFile, "SEAS", Sea.class);
  }

  @Override
  protected Stream<Sea> resultSetToStream(ResultSet resultSet) throws SQLException {
    List<Sea> seas = new ArrayList<>(0);
    
    while (resultSet.next()) {
      seas.add(Sea.builder()
              .name(resultSet.getString("NAME"))
          .build());
    }
    
    return seas.stream();
  }
}

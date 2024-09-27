package edu.colorado.cires.pace.datastore.sqlite;

import edu.colorado.cires.pace.data.object.ship.Ship;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * ShipSQLiteDatastore extends SQLiteDatastore and provides an initializer
 */
public class ShipSQLiteDatastore extends SQLiteDatastore<Ship> {

  /**
   * Initializes a ship type sqlite datastore
   * @param sqliteFile path to datastore
   */
  public ShipSQLiteDatastore(Path sqliteFile) {
    super(sqliteFile, "SHIPS", Ship.class);
  }

  @Override
  protected Stream<Ship> resultSetToStream(ResultSet resultSet) throws SQLException {
    List<Ship> ships = new ArrayList<>(0);
    
    while (resultSet.next()) {
      ships.add(Ship.builder()
              .name(resultSet.getString("NAME"))
          .build());
    }
    
    return ships.stream();
  }
}

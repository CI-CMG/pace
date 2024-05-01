package edu.colorado.cires.pace.datastore.sqlite;

import edu.colorado.cires.pace.data.object.Ship;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ShipSQLiteDatastore extends SQLiteDatastore<Ship> {

  protected ShipSQLiteDatastore(Path sqliteFile) {
    super(sqliteFile, "SHIPS");
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
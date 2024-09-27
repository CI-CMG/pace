package edu.colorado.cires.pace.datastore.sqlite;

import edu.colorado.cires.pace.data.object.detectionType.DetectionType;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * DetectionTypeSQLiteDatastore extends SQLiteDatastore and provides an initializer
 */
public class DetectionTypeSQLiteDatastore extends SQLiteDatastore<DetectionType> {

  /**
   * Initializes a detection type sqlite datastore
   * @param sqliteFile path to datastore
   */
  public DetectionTypeSQLiteDatastore(Path sqliteFile) {
    super(sqliteFile, "DETECTION_TYPES", DetectionType.class);
  }

  @Override
  protected Stream<DetectionType> resultSetToStream(ResultSet resultSet) throws SQLException {
    List<DetectionType> detectionTypes = new ArrayList<>(0);
    
    while (resultSet.next()) {
      detectionTypes.add(DetectionType.builder()
              .source(resultSet.getString("SOURCE"))
              .scienceName(resultSet.getString("SCIENCE_NAME"))
          .build());
    }
    
    return detectionTypes.stream();
  }
}

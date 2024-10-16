package edu.colorado.cires.pace.datastore.sqlite;

import edu.colorado.cires.pace.data.object.fileType.FileType;
import edu.colorado.cires.pace.data.object.instrument.Instrument;
import edu.colorado.cires.pace.datastore.Datastore;
import edu.colorado.cires.pace.datastore.DatastoreException;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * InstrumentSQLiteDatastore extends SQLiteDatastore and provides an initializer
 */
public class InstrumentSQLiteDatastore extends SQLiteDatastore<Instrument> {
  
  private final Datastore<FileType> fileTypeDatastore;

  /**
   * Initializes an instrument type sqlite datastore
   * @param sqliteFile path to datastore
   */
  public InstrumentSQLiteDatastore(Path sqliteFile, Datastore<FileType> fileTypeDatastore) {
    super(sqliteFile, "INSTRUMENTS", Instrument.class);
    this.fileTypeDatastore = fileTypeDatastore;
  }

  @Override
  protected Stream<Instrument> resultSetToStream(ResultSet resultSet) throws SQLException {
    List<Instrument> instruments = new ArrayList<>(0);
    
    while (resultSet.next()) {
      instruments.add(Instrument.builder()
              .name(resultSet.getString("Name"))
              .fileTypes(Arrays.stream(resultSet.getString("FILE_TYPES").split(","))
                  .map(ft -> {
                    try {
                      return fileTypeDatastore.findByUniqueField(ft).orElseThrow(
                          () -> new DatastoreException(String.format(
                              "file type %s not found", ft 
                          ), null)
                      );
                    } catch (DatastoreException e) {
                      throw new RuntimeException(e);
                    }
                  }).map(FileType::getType)
                  .toList())
          .build());
    }
    
    return instruments.stream();
  }
}

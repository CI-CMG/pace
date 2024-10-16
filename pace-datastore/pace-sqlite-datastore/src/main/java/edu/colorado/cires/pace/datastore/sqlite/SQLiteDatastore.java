package edu.colorado.cires.pace.datastore.sqlite;

import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.Datastore;
import edu.colorado.cires.pace.datastore.DatastoreException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import jdk.jshell.spi.ExecutionControl.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SQLiteDatastore holds an SQLite file which functions as a database
 * @param <O>
 */
public abstract class SQLiteDatastore<O extends ObjectWithUniqueField> implements Datastore<O> {
  
  private final Logger LOGGER;
  
  private final Path sqliteFile;
  private final String tableName;
  private final Class<O> clazz;

  protected SQLiteDatastore(Path sqliteFile, String tableName, Class<O> clazz) {
    this.clazz = clazz;
    LOGGER = LoggerFactory.getLogger(this.getClass());
    this.sqliteFile = sqliteFile;
    this.tableName = tableName;
  }

  /**
   * Currently unimplemented
   *
   * @param object to save
   * @return not applicable
   * @throws DatastoreException indicating failed save due to no implementation
   */
  @Override
  public O save(O object) throws DatastoreException {
    try {
      throw new NotImplementedException("save not implemented");
    } catch (NotImplementedException e) {
      throw new DatastoreException("Save failed", e);
    }
  }

  /**
   * Currently unimplemented
   *
   * @param object to delete
   * @throws DatastoreException indicating failed deletion due to no implementation
   */
  @Override
  public void delete(O object) throws DatastoreException {
    try {
      throw new NotImplementedException("delete not implemented");
    } catch (NotImplementedException e) {
      throw new DatastoreException("Delete failed", e);
    }
  }

  /**
   * Currently unimplemented
   *
   * @param uuid to find by
   * @return matching object
   * @throws DatastoreException indicating failed search due to no implementation
   */
  @Override
  public Optional<O> findByUUID(UUID uuid) throws DatastoreException {
    try {
      throw new NotImplementedException("findByUUID not implemented");
    } catch (NotImplementedException e) {
      throw new DatastoreException("Find by uuid failed", e);
    }
  }

  /**
   * Currently unimplemented
   *
   * @param uniqueField to find by
   * @return matching object
   * @throws DatastoreException indicating failed search due to no implementation
   */
  @Override
  public Optional<O> findByUniqueField(String uniqueField) throws DatastoreException {
    try {
      throw new NotImplementedException("findByUniqueField not implemented");
    } catch (NotImplementedException e) {
      throw new DatastoreException("Find by unique field failed", e);
    }
  }

  /**
   * Currently unimplemented
   *
   * @return stream of all objects in datastore
   * @throws DatastoreException indicating failed search due to no implementation
   */
  @Override
  public Stream<O> findAll() throws DatastoreException {
    LOGGER.debug("Listing {} contents from {}", tableName, sqliteFile);
    try (Connection connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", sqliteFile.toString())); Statement statement = connection.createStatement()) {
      statement.setQueryTimeout(30);
      
      return resultSetToStream(
          statement.executeQuery(String.format("select * from %s", tableName))
      );
    } catch (SQLException e) {
      throw new DatastoreException("Failed to list table rows", e);
    }
  }
  
  protected abstract Stream<O> resultSetToStream(ResultSet resultSet) throws SQLException;

  /**
   * Returns null to indicate lack of unique field name
   * @return null
   */
  @Override
  public String getUniqueFieldName() {
    return null;
  }

  /**
   * Returns simple class name
   * @return String class simplified name
   */
  @Override
  public String getClassName() {
    return clazz.getSimpleName();
  }

}

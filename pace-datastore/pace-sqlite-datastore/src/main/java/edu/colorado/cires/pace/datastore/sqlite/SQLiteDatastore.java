package edu.colorado.cires.pace.datastore.sqlite;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
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
import java.util.function.Function;
import java.util.stream.Stream;
import jdk.jshell.spi.ExecutionControl.NotImplementedException;

public abstract class SQLiteDatastore<O extends ObjectWithUniqueField> implements Datastore<O> {
  
  private final Path sqliteFile;
  private final String tableName;

  protected SQLiteDatastore(Path sqliteFile, String tableName) {
    this.sqliteFile = sqliteFile;
    this.tableName = tableName;
  }

  @Override
  public O save(O object) throws DatastoreException {
    try {
      throw new NotImplementedException("save not implemented");
    } catch (NotImplementedException e) {
      throw new DatastoreException("Save failed", e);
    }
  }

  @Override
  public void delete(O object) throws DatastoreException {
    try {
      throw new NotImplementedException("delete not implemented");
    } catch (NotImplementedException e) {
      throw new DatastoreException("Delete failed", e);
    }
  }

  @Override
  public Optional<O> findByUUID(UUID uuid) throws DatastoreException {
    try {
      throw new NotImplementedException("findByUUID not implemented");
    } catch (NotImplementedException e) {
      throw new DatastoreException("Find by uuid failed", e);
    }
  }

  @Override
  public Optional<O> findByUniqueField(String uniqueField) throws DatastoreException {
    try {
      throw new NotImplementedException("findByUniqueField not implemented");
    } catch (NotImplementedException e) {
      throw new DatastoreException("Find by unique field failed", e);
    }
  }

  @Override
  public Stream<O> findAll() throws DatastoreException {
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

  @Override
  public String getUniqueFieldName() {
    return null;
  }

  @Override
  public String getClassName() {
    return null;
  }

  @Override
  public Function<O, String> getUniqueFieldGetter() {
    return null;
  }
}

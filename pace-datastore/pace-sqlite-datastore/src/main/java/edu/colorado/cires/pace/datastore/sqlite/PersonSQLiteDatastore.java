package edu.colorado.cires.pace.datastore.sqlite;

import edu.colorado.cires.pace.data.object.Person;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class PersonSQLiteDatastore extends SQLiteDatastore<Person> {

  public PersonSQLiteDatastore(Path sqliteFile) {
    super(sqliteFile, "PEOPLE", Person.class);
  }

  @Override
  protected Stream<Person> resultSetToStream(ResultSet resultSet) throws SQLException {
    List<Person> people = new ArrayList<>(0);

    SQLException sqlException = new SQLException("Failed to create objects");
    
    while (resultSet.next()) {
      try {
        people.add(Person.builder()
            .name(resultSet.getString("NAME"))
            .street(resultSet.getString("STREET"))
            .city(resultSet.getString("CITY"))
            .state(resultSet.getString("STATE"))
            .zip(resultSet.getString("ZIP"))
            .country(resultSet.getString("COUNTRY"))
            .email(resultSet.getString("EMAIL"))
            .phone(resultSet.getString("PHONE"))
            .uuid(UUID.fromString(resultSet.getString("UUID")))
            .orcid(resultSet.getString("ORCID"))
            .organization(resultSet.getString("ORGANIZATION"))
            .position(resultSet.getString("POSITION"))
            .build());
      } catch (IllegalArgumentException e) {
        sqlException.addSuppressed(new SQLException(String.format(
            "Failed to create Person: %s", e.getMessage()
        )));
      }
    }
    
    if (sqlException.getSuppressed().length > 0) {
      throw sqlException;
    }
    
    return people.stream();
  }
}

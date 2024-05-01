package edu.colorado.cires.pace.datastore.sqlite;

import edu.colorado.cires.pace.data.object.Organization;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class OrganizationsSQLiteDatastore extends SQLiteDatastore<Organization> {

  public OrganizationsSQLiteDatastore(Path sqliteFile) {
    super(sqliteFile, "ORGANIZATIONS");
  }

  @Override
  protected Stream<Organization> resultSetToStream(ResultSet resultSet) throws SQLException {
    List<Organization> organizations = new ArrayList<>(0);
    
    while (resultSet.next()) {
      organizations.add(Organization.builder()
              .name(resultSet.getString("NAME"))
              .street(resultSet.getString("STREET"))
              .city(resultSet.getString("CITY"))
              .state(resultSet.getString("STATE"))
              .zip(resultSet.getString("ZIP"))
              .country(resultSet.getString("COUNTRY"))
              .email(resultSet.getString("EMAIL"))
              .phone(resultSet.getString("PHONE"))
              .uuid(UUID.fromString(resultSet.getString("UUID")))
          .build());
    }
    
    return organizations.stream();
  }
}

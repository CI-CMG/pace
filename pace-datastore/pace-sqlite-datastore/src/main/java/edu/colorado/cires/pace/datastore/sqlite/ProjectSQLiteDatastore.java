package edu.colorado.cires.pace.datastore.sqlite;

import edu.colorado.cires.pace.data.object.project.Project;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ProjectSQLiteDatastore extends SQLiteDatastore<Project> {

  public ProjectSQLiteDatastore(Path sqliteFile) {
    super(sqliteFile, "PROJECTS", Project.class);
  }

  @Override
  protected Stream<Project> resultSetToStream(ResultSet resultSet) throws SQLException {
    List<Project> projects = new ArrayList<>(0);
    
    while (resultSet.next()) {
      projects.add(Project.builder()
              .name(resultSet.getString("NAME"))
          .build());
    }
    
    return projects.stream();
  }
}

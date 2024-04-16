package edu.colorado.cires.pace.data.object;

import java.time.LocalDateTime;
import java.util.List;

public interface PackageInfo {
  Person getDatasetPackager();
  List<Project> getProjects();
  LocalDateTime getPublicReleaseDate();
}

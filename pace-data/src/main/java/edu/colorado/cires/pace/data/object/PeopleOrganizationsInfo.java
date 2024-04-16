package edu.colorado.cires.pace.data.object;

import java.util.List;

public interface PeopleOrganizationsInfo {
  List<Person> getScientists();
  List<Organization> getSponsors();
  List<Organization> getFunders();
}

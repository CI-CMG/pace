package edu.colorado.cires.pace.data.object;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public interface PeopleOrganizationsInfo {
  @NotNull @NotEmpty
  List<@Valid Person> getScientists();
  @NotNull @NotEmpty
  List<@Valid Organization> getSponsors();
  @NotNull @NotEmpty
  List<@Valid Organization> getFunders();
}

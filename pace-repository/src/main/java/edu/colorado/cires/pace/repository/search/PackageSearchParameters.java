package edu.colorado.cires.pace.repository.search;

import edu.colorado.cires.pace.data.object.Package;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Locale;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class PackageSearchParameters implements SearchParameters<Package> {
  
  private final List<@NotBlank String> packageIds;

  @Override
  public boolean matches(Package object) {
    if (packageIds.isEmpty()) {
      return true;
    }
    String packageId = object.getPackageId().toLowerCase(Locale.ROOT);
    return packageIds.stream()
        .anyMatch(pid -> packageId.contains(
            pid.toLowerCase(Locale.ROOT)
        ));
  }
}

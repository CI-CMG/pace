package edu.colorado.cires.pace.repository.search;

import edu.colorado.cires.pace.data.object.DetectionType;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Locale;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class DetectionTypeSearchParameters implements SearchParameters<DetectionType> {
  
  private final List<@NotBlank String> sources;

  @Override
  public boolean matches(DetectionType object) {
    String source = object.getSource().toLowerCase(Locale.ROOT);
    return sources.stream()
        .anyMatch(s -> source.contains(s.toLowerCase(Locale.ROOT)));
  }
}

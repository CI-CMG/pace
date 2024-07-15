package edu.colorado.cires.pace.repository.search;

import edu.colorado.cires.pace.data.object.FileType;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Locale;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class FileTypeSearchParameters implements SearchParameters<FileType> {
  
  private final List<@NotBlank String> types;

  @Override
  public boolean matches(FileType object) {
    if (types.isEmpty()) {
      return true;
    }
    String type = object.getType().toLowerCase(Locale.ROOT);
    return types.stream()
        .anyMatch(t -> type.contains(t.toLowerCase(Locale.ROOT)));
  }
}

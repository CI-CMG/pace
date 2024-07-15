package edu.colorado.cires.pace.repository.search;

import edu.colorado.cires.pace.data.object.ObjectWithName;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Locale;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public abstract class ObjectWithNameSearchParameters<O extends ObjectWithName> implements SearchParameters<O> {
  
  private final List<@NotBlank String> names;

  @Override
  public boolean matches(O object) {
    String name = object.getName().toLowerCase(Locale.ROOT);
    return names.stream()
        .anyMatch(n -> n.toLowerCase(Locale.ROOT).contains(name));
  }
}

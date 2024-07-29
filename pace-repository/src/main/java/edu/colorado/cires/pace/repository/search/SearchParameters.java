package edu.colorado.cires.pace.repository.search;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class SearchParameters<O extends AbstractObject> {
  
  @Default
  private final List<@NotBlank String> uniqueFields = new ArrayList<>(0);
  @Default
  List<@NotNull Boolean> visibilityStates = new ArrayList<>(0);
  
  public boolean matches(O object) {
    String uniqueField = object.getUniqueField().toLowerCase(Locale.ROOT);
    boolean uniqueFieldMatch = uniqueFields.isEmpty() || uniqueFields.stream()
        .anyMatch(n -> uniqueField.contains(
            n.toLowerCase(Locale.ROOT)
        ));
    
    boolean visibilityMatch = visibilityStates.isEmpty() || visibilityStates.stream()
        .anyMatch(v -> v.equals(object.isVisible()));
    
    return visibilityMatch && uniqueFieldMatch;
  }

}

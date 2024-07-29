package edu.colorado.cires.pace.data.object.project;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.ObjectWithName;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class Project extends ObjectWithName {

  @Override
  public AbstractObject setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public Project setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}

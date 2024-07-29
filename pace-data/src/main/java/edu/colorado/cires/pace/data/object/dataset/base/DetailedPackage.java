package edu.colorado.cires.pace.data.object.dataset.base;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import java.util.Collections;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public abstract class DetailedPackage extends BasePackage<AbstractObject> {
  private final AbstractObject datasetPackager;
  private final List<AbstractObject> scientists;
  private final List<AbstractObject> projects;
  private final List<AbstractObject> sponsors;
  private final List<AbstractObject> funders;
  private final AbstractObject platform;
  private final AbstractObject instrument;

  @Override
  protected List<String> getProjectNames() {
    return getProjects() == null ? Collections.emptyList() : getProjects().stream()
        .map(AbstractObject::getUniqueField)
        .toList();
  }
}

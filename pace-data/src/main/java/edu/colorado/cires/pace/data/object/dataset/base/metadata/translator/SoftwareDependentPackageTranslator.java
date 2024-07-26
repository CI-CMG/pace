package edu.colorado.cires.pace.data.object.dataset.base.metadata.translator;

import edu.colorado.cires.pace.data.object.dataset.base.translator.PackageTranslator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
public abstract class SoftwareDependentPackageTranslator extends PackageTranslator {

  private final String softwareNames;
  private final String softwareVersions;
  private final String softwareProtocolCitation;
  private final String softwareDescription;
  private final String softwareProcessingDescription;

}

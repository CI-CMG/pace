package edu.colorado.cires.pace.data.object.dataset.soundPropagationModels;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.AudioTimeRange;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.SoftwareDescription;

/**
 * BaseSoundPropagationModelsPackage extends SoftwareDescription and AudioTimeRange and adds a getter for
 * modeled frequency
 */
public interface BaseSoundPropagationModelsPackage extends SoftwareDescription, AudioTimeRange {
  Float getModeledFrequency();
}

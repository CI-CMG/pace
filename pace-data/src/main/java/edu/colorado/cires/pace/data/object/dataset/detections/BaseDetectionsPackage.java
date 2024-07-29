package edu.colorado.cires.pace.data.object.dataset.detections;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.AnalysisDescription;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.DataQuality;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.SoftwareDescription;

public interface BaseDetectionsPackage<T> extends DataQuality<T>, SoftwareDescription, AnalysisDescription  {

  BaseDetectionsPackage<T> setSoundSource(T soundSource);

  T getSoundSource();
}

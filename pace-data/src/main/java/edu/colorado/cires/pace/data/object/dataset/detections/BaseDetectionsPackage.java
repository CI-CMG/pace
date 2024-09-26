package edu.colorado.cires.pace.data.object.dataset.detections;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.AnalysisDescription;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.DataQuality;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.SoftwareDescription;

/**
 * BaseDetectionsPackage extends DataQuality, SoftwareDescription, and AnalysisDescription
 * in addition to adding in a setter and getter for soundSource
 *
 * @param <T> Type of BaseDetectionsPackage
 */
public interface BaseDetectionsPackage<T> extends DataQuality<T>, SoftwareDescription, AnalysisDescription  {

  /**
   * Returns package with sound source set to provided value
   *
   * @param soundSource to set in returned package
   * @return BaseDetectionsPackage with soundSource set
   */
  BaseDetectionsPackage<T> setSoundSource(T soundSource);

  /**
   * Returns soundSource of package
   *
   * @return T soundSource of package
   */
  T getSoundSource();
}

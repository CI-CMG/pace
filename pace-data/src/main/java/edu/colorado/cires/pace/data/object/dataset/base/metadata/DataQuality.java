package edu.colorado.cires.pace.data.object.dataset.base.metadata;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DataQualityEntry;
import java.util.List;

/**
 * DataQuality provides getters for quality entry fields and a setter
 * for quality analyst
 *
 * @param <T> data quality type
 */
public interface DataQuality<T> {

  /**
   * Returns quality analyst
   * @return T quality analyst
   */
  T getQualityAnalyst();

  /**
   * Returns quality analysis objectives
   * @return String quality analysis objectives
   */
  String getQualityAnalysisObjectives();

  /**
   * Returns quality analysis method
   * @return String quality analysis method
   */
  String getQualityAnalysisMethod();

  /**
   * Returns quality assessment description
   * @return String quality assessment description
   */
  String getQualityAssessmentDescription();

  /**
   * Returns quality entries
   * @return List quality entries
   */
  List<DataQualityEntry> getQualityEntries();

  /**
   * Returns a new object with the provided quality analyst field set
   *
   * @param qualityAnalyst to set in new object
   * @return DataQuality object with provided quality analyst set
   */
  DataQuality<T> setQualityAnalyst(T qualityAnalyst);

}

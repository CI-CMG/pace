package edu.colorado.cires.pace.data.object.dataset.base.metadata;

/**
 * AnalysisDescription extends FrequencyRange and adds in getters for
 * analysis fields
 */
public interface AnalysisDescription extends FrequencyRange {

  /**
   * Returns analysis time zone
   * @return Integer analysis time zone
   */
  Integer getAnalysisTimeZone();

  /**
   * Returns analysis effort
   * @return Integer analysis effort
   */
  Integer getAnalysisEffort();

  /**
   * Returns sample rate
   * @return Float sample rate
   */
  Float getSampleRate();

}

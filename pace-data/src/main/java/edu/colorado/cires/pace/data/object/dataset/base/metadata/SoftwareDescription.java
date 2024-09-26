package edu.colorado.cires.pace.data.object.dataset.base.metadata;

/**
 * SoftwareDescription provides getters for relevant software fields
 */
public interface SoftwareDescription {

  /**
   * Returns software names
   * @return String software names
   */
  String getSoftwareNames();

  /**
   * Returns software versions
   * @return String software versions
   */
  String getSoftwareVersions();

  /**
   * Returns software protocol citation
   * @return String software protocol citation
   */
  String getSoftwareProtocolCitation();

  /**
   * Returns software description
   * @return String software description
   */
  String getSoftwareDescription();

  /**
   * Returns software processing description
   * @return String software processing decription
   */
  String getSoftwareProcessingDescription();

}

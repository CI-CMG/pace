package edu.colorado.cires.pace.data.object.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;

/**
 * AbstractObject outlines some required fields and methods for
 * objects within PACE
 */
public interface AbstractObject {

  /**
   * Returns the unique field
   *
   * @return unique field in the form of a string
   */
  @JsonIgnore
  String getUniqueField();

  /**
   * Creates an object which is identical besides the newly set uuid
   *
   * @param uuid field for assigning uuid to new object
   * @return new abstract object with the provided uuid
   */
  AbstractObject setUuid(UUID uuid);

  /**
   * Creates an object which is identical besides the newly set visibility
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return an abstract object which has visibility dictated
   */
  AbstractObject setVisible(boolean visible);

  /**
   * Returns the object's uuid
   *
   * @return the object's uuid
   */
  UUID getUuid();

  /**
   * Returns a boolean indicating if the object is visible
   *
   * @return boolean indicating if the object is visible
   */
  boolean isVisible();
}

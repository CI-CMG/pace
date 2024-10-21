package edu.colorado.cires.pace.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;

/**
 * ConflictException extends Exception and indicates an object
 * with the provided uuid already exists
 */
public class ConflictException extends Exception {

  private final Object existingObject;
  private final AudioPackage newObject;

  /**
   * Throws a new conflict exception
   * @param message to attach to error output
   * @param existingObject existing object with conflicting uuid
   * @param newObject new object with conflicting uuid
   */
  public ConflictException(String message, Object existingObject, Object newObject) {
    super(message);
    this.existingObject = existingObject;
    if (newObject instanceof AudioPackage newOb && existingObject instanceof AudioPackage existingOb) {
      this.newObject = newOb.setUuid(existingOb.getUuid());
    } else {
      this.newObject = null;
    }
  }

  /**
   * Checks whether the provided new object is identical to the existing object
   * @return boolean indicating if identical
   * @throws JsonProcessingException thrown in case of error mapping to json format
   */
  public boolean checkIdentical() throws JsonProcessingException {
    if (existingObject == null) {
      return false;
    }
    ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).writer().withDefaultPrettyPrinter();
    String jsonExisting = ow.writeValueAsString(existingObject);
    String jsonNew = ow.writeValueAsString(newObject);
    return jsonExisting.equals(jsonNew);
  }
}

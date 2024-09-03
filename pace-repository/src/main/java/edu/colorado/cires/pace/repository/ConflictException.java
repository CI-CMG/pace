package edu.colorado.cires.pace.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.colorado.cires.pace.data.object.dataset.audio.AudioPackage;

public class ConflictException extends Exception {

  private final Object existingObject;
  private final AudioPackage newObject;

  public ConflictException(String message, Object existingObject, Object newObject) {
    super(message);
    this.existingObject = existingObject;
    if (newObject instanceof AudioPackage newOb && existingObject instanceof AudioPackage existingOb) {
      this.newObject = newOb.setUuid(existingOb.getUuid());
    } else {
      this.newObject = null;
    }
  }

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

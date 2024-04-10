package edu.colorado.cires.pace.data.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.Ship;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class ViolationSerializationTest {
  
  private final ObjectMapper objectMapper = new ObjectMapper();
  
  @Test
  void serializeViolations() throws JsonProcessingException {
    ValidationException exception = new ValidationException(Ship.class, Set.of(
        ConstraintViolation.builder()
            .message("violation1")
            .property("property1")
            .build(),
        ConstraintViolation.builder()
            .message("violation2")
            .property("property2")
            .build()
    ));
    
    String json = objectMapper.writeValueAsString(exception.getViolations());
    
    objectMapper.readValue(json, new TypeReference<>() {});
  }

}

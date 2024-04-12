package edu.colorado.cires.pace.translator;

import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.data.validation.ConstraintViolation;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

final class TranslatorUtils {

  public static <O> O convertMapToObject(Map<String, Optional<String>> propertyMap, Class<O> clazz)
      throws ValidationException, TranslationException {
    if (clazz.isAssignableFrom(Ship.class)) {
      return (O) shipFromMap(propertyMap);
    } else {
      throw new TranslationException(String.format(
          "Translation not supported for %s", clazz.getSimpleName()
      ));
    }
  }
  
  private static Ship shipFromMap(Map<String, Optional<String>> propertyMap) throws ValidationException {
    
    Set<ConstraintViolation> violations = new HashSet<>(0);
    
    UUID uuid = null;
    try {
      uuid = uuidFromString(getProperty(propertyMap, "uuid"));
    } catch (ValidationException e) {
      violations.addAll(e.getViolations());
    }
    
    try {
      Ship ship = Ship.builder()
          .uuid(uuid)
          .name(getProperty(propertyMap, "name"))
          .build();
      
      if (!violations.isEmpty()) {
        throw new ValidationException(Ship.class, violations);
      }
      
      return ship;
    } catch (ValidationException e) {
      violations.addAll(e.getViolations());
      throw new ValidationException(Ship.class, violations);
    }
  }
  
  private static UUID uuidFromString(String uuidString) throws ValidationException {
    if (uuidString == null) {
      return null;
    }
    
    try {
      return UUID.fromString(uuidString);
    } catch (IllegalArgumentException e) {
      throw new ValidationException(UUID.class, Set.of(
          ConstraintViolation.builder()
              .property("uuid")
              .message("invalid uuid format")
              .build()
      ));
    }
  }
  
  private static String getProperty(Map<String, Optional<String>> map, String property) {
    Optional<String> value = map.get(property);
    if (value == null) {
      return null;
    }
    
    return value.orElse(null);
  }
}

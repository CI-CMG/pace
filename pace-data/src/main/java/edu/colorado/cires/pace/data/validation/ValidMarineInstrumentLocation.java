package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.MarineInstrumentLocation;
import edu.colorado.cires.pace.data.validation.ValidMarineInstrumentLocation.ValidMarineInstrumentLocationValidator;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ValidMarineInstrumentLocation outlines the structure for the validation
 * of a marine instrument location
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidMarineInstrumentLocationValidator.class)
@Documented
public @interface ValidMarineInstrumentLocation {
  
  String message() default "invalid marine instrument location";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};

  /**
   * ValidMarineInstrumentLocationValidator checks the validity of a marine instrument location
   */
  class ValidMarineInstrumentLocationValidator implements ConstraintValidator<ValidMarineInstrumentLocation, MarineInstrumentLocation> {

    /**
     * Indicates whether a location contains a valid value
     *
     * @param value the location to validate
     * @param context of the location to write errors to if invalid
     * @return boolean indicating the validity of the location
     */
    @Override
    public boolean isValid(MarineInstrumentLocation value, ConstraintValidatorContext context) {
      Float seaFloorDepth = value.getSeaFloorDepth();
      Float instrumentDepth = value.getInstrumentDepth();
      
      if (instrumentDepth == null) {
        context.disableDefaultConstraintViolation();
        
        context.buildConstraintViolationWithTemplate("must not be null")
          .addPropertyNode("instrumentDepth")
          .addConstraintViolation();
        
        return false;
      } else {
        if (seaFloorDepth != null && seaFloorDepth > instrumentDepth) {
          context.disableDefaultConstraintViolation();
          
          context.buildConstraintViolationWithTemplate("must be less than or equal to instrumentDepth")
              .addPropertyNode("seaFloorDepth")
              .addConstraintViolation();
          
          context.buildConstraintViolationWithTemplate("must be greater than or equal to seaFloorDepth")
              .addPropertyNode("instrumentDepth")
              .addConstraintViolation();
          
          return false;
        }
      }
      
      return true;
    }
  }

}

package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.location.ElevationPair;
import edu.colorado.cires.pace.data.validation.ValidElevationPair.ValidElevationPairValidator;
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
 * ValidElevationPair outlines the structure for the validation of an elevation pair
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidElevationPairValidator.class)
@Documented
public @interface ValidElevationPair {
  
  String message() default "invalid elevation pair";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};

  /**
   * ValidElevationPairValidator checks the validity of an elevation pair
   */
  class ValidElevationPairValidator implements ConstraintValidator<ValidElevationPair, ElevationPair> {

    /**
     * Indicates whether an elevation pair contains valid values
     *
     * @param value the elevation pair to validate
     * @param context of the elevation pair to write errors to if invalid
     * @return boolean indicating the validity of the elevation pair
     */
    @Override
    public boolean isValid(ElevationPair value, ConstraintValidatorContext context) {
      Float instrumentElevation = value.getInstrumentElevation();
      Float surfaceElevation = value.getSurfaceElevation();
      
      if (instrumentElevation == null || surfaceElevation == null) {
        context.disableDefaultConstraintViolation();
        
        if (instrumentElevation == null) {
          context.buildConstraintViolationWithTemplate("must not be null")
              .addPropertyNode("instrumentElevation")
              .addConstraintViolation();
        }
        
        if (surfaceElevation == null) {
          context.buildConstraintViolationWithTemplate("must not be null")
              .addPropertyNode("surfaceElevation")
              .addConstraintViolation();
        }
        
        return false;
      } else {
        if (instrumentElevation < surfaceElevation) {
          context.disableDefaultConstraintViolation();
          
          context.buildConstraintViolationWithTemplate("must be less than or equal to instrumentElevation")
              .addPropertyNode("surfaceElevation")
              .addConstraintViolation();
          
          context.buildConstraintViolationWithTemplate("must be greater than or equal to surfaceElevation")
              .addPropertyNode("instrumentElevation")
              .addConstraintViolation();
          
          return false;
        }
      }
      
      return true;
    }
  }

}

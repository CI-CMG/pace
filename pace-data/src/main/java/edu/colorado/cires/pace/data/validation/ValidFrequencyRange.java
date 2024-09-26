package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.FrequencyRange;
import edu.colorado.cires.pace.data.validation.ValidFrequencyRange.ValidFrequencyRangeValidator;
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
 * ValidFrequencyRange outlines the structure for the validation of a frequency
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidFrequencyRangeValidator.class)
@Documented
public @interface ValidFrequencyRange {
  
  String message() default "Invalid frequency range";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};

  /**
   * ValidFrequencyRangeValidator checks the validity of a frequency
   */
  class ValidFrequencyRangeValidator implements ConstraintValidator<ValidFrequencyRange, FrequencyRange> {

    /**
     * Indicates whether a frequency contains valid value
     *
     * @param value the frequency to validate
     * @param context of the frequency to write errors to if invalid
     * @return boolean indicating the validity of the frequency
     */
    @Override
    public boolean isValid(FrequencyRange value, ConstraintValidatorContext context) {
      Float minFrequency = value.getMinFrequency();
      Float maxFrequency = value.getMaxFrequency();
      
      if (minFrequency == null || maxFrequency == null) {
        context.disableDefaultConstraintViolation();
        
        if (minFrequency == null) {
          context.buildConstraintViolationWithTemplate("must not be null")
              .addPropertyNode("minFrequency")
              .addConstraintViolation();
        }
        
        if (maxFrequency == null) {
          context.buildConstraintViolationWithTemplate("must not be null")
              .addPropertyNode("maxFrequency")
              .addConstraintViolation();
        }
        
        return false;
      } else {
        if (maxFrequency < minFrequency) {
          context.disableDefaultConstraintViolation();
          
          context.buildConstraintViolationWithTemplate("must be less than or equal to maxFrequency")
              .addPropertyNode("minFrequency")
              .addConstraintViolation();
          context.buildConstraintViolationWithTemplate("must be greater than or equal to minFrequency")
              .addPropertyNode("maxFrequency")
              .addConstraintViolation();
          
          return false;
        }
      }
      
      return true;
    }
  }

}

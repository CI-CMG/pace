package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.FrequencyRange;
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

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidFrequencyRangeValidator.class)
@Documented
public @interface ValidFrequencyRange {
  
  String message() default "Invalid frequency range";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
  
  class ValidFrequencyRangeValidator implements ConstraintValidator<ValidFrequencyRange, FrequencyRange> {

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

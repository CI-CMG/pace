package edu.colorado.cires.pace.data.validation;

import jakarta.validation.ConstraintValidatorContext;
import java.util.function.Function;

/**
 * Provides functions for ensuring the validity of objects in PACE
 */
final class ValidationUtils {

  /**
   * Provides the expected functionality of time range objects
   * @param <O> Type of time format
   */
  interface AbstractTimeRange<O> {
    String getStartTimePropertyName();
    O getStartTime();
    String getEndTimePropertyName();
    O getEndTime();
    
    Function<O, Boolean> isEqualMethod();
    Function<O, Boolean> isAfterMethod();
  }

  /**
   * Ensures the validity of time range objects
   *
   * @param value time range for evaluation
   * @param context in which the value exists
   * @return boolean evaluation of the time range validity
   * @param <O> Type of time range
   */
  public static <O> boolean validateAbstractTimeRange(AbstractTimeRange<O> value, ConstraintValidatorContext context) {
    O startTime = value.getStartTime();
    String startTimePropertyName = value.getStartTimePropertyName();
    O endTime = value.getEndTime();
    String endTimePropertyName = value.getEndTimePropertyName();

    if (startTime == null || endTime  == null) {
      return true;
//      context.disableDefaultConstraintViolation();
//
//      if (startTime == null) {
//        context.buildConstraintViolationWithTemplate("must not be null")
//            .addPropertyNode(startTimePropertyName)
//            .addConstraintViolation();
//      }
//
//      if (endTime == null) {
//        context.buildConstraintViolationWithTemplate("must not be null")
//            .addPropertyNode(endTimePropertyName)
//            .addConstraintViolation();
//      }
//
//      return false;
    } else {
      if (value.isEqualMethod() != null) {
        if (value.isEqualMethod().apply(endTime)) {
          context.disableDefaultConstraintViolation();

          context.buildConstraintViolationWithTemplate(String.format(
                  "must not equal %s", endTimePropertyName
              )).addPropertyNode(startTimePropertyName)
              .addConstraintViolation();
          context.buildConstraintViolationWithTemplate(String.format(
                  "must not equal %s", startTimePropertyName
              )).addPropertyNode(endTimePropertyName)
              .addConstraintViolation();
          return false;
        }
      }
      
      if (value.isAfterMethod().apply(endTime)) {
        context.disableDefaultConstraintViolation();
        
        context.buildConstraintViolationWithTemplate(String.format(
            "must be before%s %s", value.isEqualMethod() == null ? " or equal to" : "", endTimePropertyName
            )).addPropertyNode(startTimePropertyName)
            .addConstraintViolation();
        context.buildConstraintViolationWithTemplate(String.format(
            "must be after%s %s", value.isEqualMethod() == null ? " or equal to" : "", startTimePropertyName
            )).addPropertyNode(endTimePropertyName)
            .addConstraintViolation();
        return false;
      }
    }

    return true;
  }
  
  

}

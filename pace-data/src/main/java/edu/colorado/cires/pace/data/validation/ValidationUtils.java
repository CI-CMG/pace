package edu.colorado.cires.pace.data.validation;

import jakarta.validation.ConstraintValidatorContext;
import java.util.function.Function;

final class ValidationUtils {
  
  interface AbstractTimeRange<O> {
    String getStartTimePropertyName();
    O getStartTime();
    String getEndTimePropertyName();
    O getEndTime();
    
    Function<O, Boolean> isEqualMethod();
    Function<O, Boolean> isAfterMethod();
  }

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

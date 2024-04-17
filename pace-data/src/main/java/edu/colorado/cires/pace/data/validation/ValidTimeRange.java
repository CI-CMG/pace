package edu.colorado.cires.pace.data.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import edu.colorado.cires.pace.data.object.TimeRange;
import edu.colorado.cires.pace.data.validation.ValidTimeRange.ValidTimeRangeValidator;
import edu.colorado.cires.pace.data.validation.ValidationUtils.AbstractTimeRange;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.LocalDateTime;
import java.util.function.Function;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = ValidTimeRangeValidator.class)
@Documented
public @interface ValidTimeRange {

  String message() default "Invalid time range";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
  
  class ValidTimeRangeValidator implements ConstraintValidator<ValidTimeRange, TimeRange> {

    @Override
    public boolean isValid(TimeRange value, ConstraintValidatorContext context) {
      return ValidationUtils.validateAbstractTimeRange(new AbstractTimeRange<LocalDateTime>() {
        @Override
        public String getStartTimePropertyName() {
          return "startTime";
        }

        @Override
        public LocalDateTime getStartTime() {
          return value.getStartTime();
        }

        @Override
        public String getEndTimePropertyName() {
          return "endTime";
        }

        @Override
        public LocalDateTime getEndTime() {
          return value.getEndTime();
        }

        @Override
        public Function<LocalDateTime, Boolean> isEqualMethod() {
          return value.getStartTime()::isEqual;
        }

        @Override
        public Function<LocalDateTime, Boolean> isAfterMethod() {
          return value.getStartTime()::isAfter;
        }
      }, context);
    }
  }

}

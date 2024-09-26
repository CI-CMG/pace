package edu.colorado.cires.pace.data.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.TimeRange;
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

/**
 * ValidTimeRange outlines the structure for the validation of a time range
 */
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = ValidTimeRangeValidator.class)
@Documented
public @interface ValidTimeRange {

  String message() default "Invalid time range";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /**
   * ValidTimeRangeValidator checks the validity of a time range
   */
  class ValidTimeRangeValidator implements ConstraintValidator<ValidTimeRange, TimeRange> {

    /**
     * Checks the validity of a time range
     *
     * @param value to check the validity of
     * @param context of the value
     * @return boolean indicating the validity of the value
     */
    @Override
    public boolean isValid(TimeRange value, ConstraintValidatorContext context) {
      return ValidationUtils.validateAbstractTimeRange(new AbstractTimeRange<LocalDateTime>() {
        /**
         * Returns start time property name
         * @return String startTime
         */
        @Override
        public String getStartTimePropertyName() {
          return "startTime";
        }

        /**
         * Returns the value held in startTime
         * @return LocalDate start time
         */
        @Override
        public LocalDateTime getStartTime() {
          return value.getStartTime();
        }

        /**
         * Returns end time property name
         * @return String endTime
         */
        @Override
        public String getEndTimePropertyName() {
          return "endTime";
        }

        /**
         * Returns the value held in endTime
         * @return LocalDate end time
         */
        @Override
        public LocalDateTime getEndTime() {
          return value.getEndTime();
        }

        /**
         * Returns a function which finds if a provided value is equal to the provided date
         * @return Function of LocalDate and Boolean which checks if a value is equal to the provided date
         */
        @Override
        public Function<LocalDateTime, Boolean> isEqualMethod() {
          return value.getStartTime()::isEqual;
        }

        /**
         * Returns a function which finds if a provided value is after the provided date
         * @return Function of LocalDate and Boolean which checks if a value is after the provided date
         */
        @Override
        public Function<LocalDateTime, Boolean> isAfterMethod() {
          return value.getStartTime()::isAfter;
        }
      }, context);
    }
  }

}

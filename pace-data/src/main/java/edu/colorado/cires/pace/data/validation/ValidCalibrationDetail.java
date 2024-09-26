package edu.colorado.cires.pace.data.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.CalibrationDetail;
import edu.colorado.cires.pace.data.validation.ValidCalibrationDetail.ValidCalibrationDetailValidator;
import edu.colorado.cires.pace.data.validation.ValidationUtils.AbstractTimeRange;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.util.function.Function;

/**
 * Outlines the structure for the validation of a calibration detail
 */
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = ValidCalibrationDetailValidator.class)
@Documented
public @interface ValidCalibrationDetail {

  String message() default "Invalid calibration detail";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /**
   * Outlines the structure for checking the validity of a ValidCalibrationDetail/CalibrationDetail
   */
  class ValidCalibrationDetailValidator implements ConstraintValidator<ValidCalibrationDetail, CalibrationDetail> {

    /**
     * Checks the validity of a calibration detail
     *
     * @param value to check the validity of
     * @param context of the value
     * @return boolean indicating the validity of the value
     */
    @Override
    public boolean isValid(CalibrationDetail value, ConstraintValidatorContext context) {
      return ValidationUtils.validateAbstractTimeRange(new AbstractTimeRange<LocalDate>() {
        /**
         * Returns start time property name
         * @return String preDeploymentCalibrationDate
         */
        @Override
        public String getStartTimePropertyName() {
          return "preDeploymentCalibrationDate";
        }

        /**
         * Returns the value held in preDeploymentCalibrationDate
         * @return LocalDate pre deployment calibration date
         */
        @Override
        public LocalDate getStartTime() {
          return value.getPreDeploymentCalibrationDate();
        }

        /**
         * Returns end time property name
         * @return String postDeploymentCalibrationDate
         */
        @Override
        public String getEndTimePropertyName() {
          return "postDeploymentCalibrationDate";
        }

        /**
         * Returns the value held in postDeploymentCalibrationDate
         * @return LocalDate post deployment calibration date
         */
        @Override
        public LocalDate getEndTime() {
          return value.getPostDeploymentCalibrationDate();
        }

        /**
         * Returns null
         * @return null
         */
        @Override
        public Function<LocalDate, Boolean> isEqualMethod() {
          return null;
        }

        /**
         * Returns a function which finds if a provided value is after the provided date
         * @return Function of LocalDate and Boolean which checks if a value is after the provided date
         */
        @Override
        public Function<LocalDate, Boolean> isAfterMethod() {
          return value.getPreDeploymentCalibrationDate()::isAfter;
        }
      }, context);
    }
  }

}

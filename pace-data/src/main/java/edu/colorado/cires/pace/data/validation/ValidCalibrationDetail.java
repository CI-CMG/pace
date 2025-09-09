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

      if (value.getPreDeploymentCalibrationDate() != null &&
          value.getPostDeploymentCalibrationDate() != null &&
          value.getPreDeploymentCalibrationDate().isAfter(value.getPostDeploymentCalibrationDate())) {
        context.disableDefaultConstraintViolation();

        context.buildConstraintViolationWithTemplate(
            "must be before or equal to postDeploymentCalibrationDate"
            ).addPropertyNode("preDeploymentCalibrationDate")
            .addConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                "must be after or equal to preDeploymentCalibrationDate"
            ).addPropertyNode("postDeploymentCalibrationDate")
            .addConstraintViolation();
        return false;
      }
      return true;
    }
  }

}

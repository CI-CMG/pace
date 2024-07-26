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

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = ValidCalibrationDetailValidator.class)
@Documented
public @interface ValidCalibrationDetail {

  String message() default "Invalid calibration detail";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
  
  class ValidCalibrationDetailValidator implements ConstraintValidator<ValidCalibrationDetail, CalibrationDetail> {

    @Override
    public boolean isValid(CalibrationDetail value, ConstraintValidatorContext context) {
      return ValidationUtils.validateAbstractTimeRange(new AbstractTimeRange<LocalDate>() {
        @Override
        public String getStartTimePropertyName() {
          return "preDeploymentCalibrationDate";
        }

        @Override
        public LocalDate getStartTime() {
          return value.getPreDeploymentCalibrationDate();
        }

        @Override
        public String getEndTimePropertyName() {
          return "postDeploymentCalibrationDate";
        }

        @Override
        public LocalDate getEndTime() {
          return value.getPostDeploymentCalibrationDate();
        }

        @Override
        public Function<LocalDate, Boolean> isEqualMethod() {
          return null;
        }

        @Override
        public Function<LocalDate, Boolean> isAfterMethod() {
          return value.getPreDeploymentCalibrationDate()::isAfter;
        }
      }, context);
    }
  }

}

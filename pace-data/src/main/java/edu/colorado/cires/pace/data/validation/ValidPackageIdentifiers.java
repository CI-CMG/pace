package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.data.validation.ValidPackageIdentifiers.ValidPackageIdentifiersValidator;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.apache.commons.lang3.StringUtils;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPackageIdentifiersValidator.class)
@Documented
public @interface ValidPackageIdentifiers {

  String message() default "at least dataCollectionName, site, or deploymentId required";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
  
  class ValidPackageIdentifiersValidator implements ConstraintValidator<ValidPackageIdentifiers, Package> {

    @Override
    public boolean isValid(Package aPackage, ConstraintValidatorContext context) {
      String dataCollectionName = aPackage.getDataCollectionName();
      String site = aPackage.getSiteOrCruiseName();
      String deploymentId = aPackage.getDeploymentId();
      
      if (
          StringUtils.isBlank(dataCollectionName) && 
          StringUtils.isBlank(site) &&
          StringUtils.isBlank(deploymentId)
      ) {
        context.disableDefaultConstraintViolation();
        String message = "at least dataCollectionName, site, or deploymentId required";
        context.buildConstraintViolationWithTemplate(message)
            .addPropertyNode("dataCollectionName")
            .addConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
            .addPropertyNode("site")
            .addConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
            .addPropertyNode("deploymentId")
            .addConstraintViolation();
        
        return false;
      }
      
      return true;
    }
  }

}

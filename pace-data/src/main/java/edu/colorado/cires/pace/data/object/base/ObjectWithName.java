package edu.colorado.cires.pace.data.object.base;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * ObjectWithName is an ObjectWithUniqueField object which has a
 * name as the unique field
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public abstract class ObjectWithName extends ObjectWithUniqueField {
  @NotBlank
  private final String name;

  /**
   * Returns the unique field
   *
   * @return unique field in the form of a string
   * */
  @Override
  public String getUniqueField() {
    return name;
  }

  /**
   * Returns the name field
   *
   * @return name field in the form of a string
   */
  public String getName() {return name;}
}

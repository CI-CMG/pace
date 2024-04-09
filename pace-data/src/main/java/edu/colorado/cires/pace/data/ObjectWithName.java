package edu.colorado.cires.pace.data;

public interface ObjectWithName extends ObjectWithUniqueField {
  
  String name();

  @Override
  default String uniqueField() {
    return name();
  }
}

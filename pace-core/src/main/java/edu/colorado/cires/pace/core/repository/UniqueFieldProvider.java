package edu.colorado.cires.pace.core.repository;

@FunctionalInterface
public interface UniqueFieldProvider<O, U> {
  U getUniqueField(O object);
}

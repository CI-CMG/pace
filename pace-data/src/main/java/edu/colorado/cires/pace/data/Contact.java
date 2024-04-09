package edu.colorado.cires.pace.data;

public interface Contact extends ObjectWithName {
  String street();
  String city();
  String state();
  String zip();
  String country();
  String email();
  String phone();

}

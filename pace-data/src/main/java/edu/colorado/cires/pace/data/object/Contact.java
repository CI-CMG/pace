package edu.colorado.cires.pace.data.object;

public interface Contact extends ObjectWithName {
  String getStreet();
  String getCity();
  String getState();
  String getZip();
  String getCountry();
  String getEmail();
  String getPhone();

}

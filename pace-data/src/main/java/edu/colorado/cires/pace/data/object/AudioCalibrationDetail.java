package edu.colorado.cires.pace.data.object;

public interface AudioCalibrationDetail extends CalibrationDetail {
  
  Float getHydrophoneSensitivity();
  Float getFrequencyRange();
  Float getGain();

}

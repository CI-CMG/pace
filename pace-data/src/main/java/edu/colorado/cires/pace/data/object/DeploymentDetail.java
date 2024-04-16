package edu.colorado.cires.pace.data.object;

import java.time.LocalDateTime;
import java.util.List;

public interface DeploymentDetail {
  
  LocalDateTime getDeploymentTime();
  LocalDateTime getRecoveryTime();
  String getComments();
  List<Sensor> getSensors();
  List<Channel> getChannels();
  
}

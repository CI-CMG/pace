package edu.colorado.cires.pace.data.object.dataset.base;

import java.util.List;

public interface DatasetDependencies<P> {
  
  P getDatasetPackager();
  List<P> getScientists();

}

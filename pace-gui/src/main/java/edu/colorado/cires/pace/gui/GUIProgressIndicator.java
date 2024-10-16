package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.packaging.ProgressIndicator;
import javax.swing.BoundedRangeModel;
import javax.swing.JProgressBar;

/**
 * GUIProgressIndicator extends ProgressIndicator and tracks the progress of
 * packaging using a bar at the bottom of the GUI
 */
public class GUIProgressIndicator extends ProgressIndicator {
  
  private final JProgressBar progressBar;

  /**
   * Initializes the gui progress bar
   * @param progressBar bar which holds progress
   */
  public GUIProgressIndicator(JProgressBar progressBar) {
    this.progressBar = progressBar;
    BoundedRangeModel progressBarModel = progressBar.getModel();
    progressBarModel.setMinimum(0);
    progressBarModel.setMaximum(100);
  }
  
  @Override
  protected void indicateStatus(int percentComplete) {
    if (percentComplete == Integer.MAX_VALUE) {
      progressBar.setIndeterminate(true);
    } else {
      progressBar.setIndeterminate(false);
      progressBar.getModel().setValue(percentComplete);
    }
  }
}

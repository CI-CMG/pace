package edu.colorado.cires.pace.packaging;

/**
 * ProgressIndicator tracks the progress of different actions in pace
 * and provides feedback on what percentage of the actions are left to
 * complete
 */
public abstract class ProgressIndicator {
  
  private long totalRecords = 0;
  private long processedRecords = 0;

  private int previousPercentComplete = 0;

  /**
   * Sets the total number of records
   * @param totalRecords number to set
   */
  public void setTotalRecords(long totalRecords) {
    this.totalRecords = totalRecords;
  }

  /**
   * Gets the total number of records
   * @return long number of records
   */
  public long getTotalRecords() {
    return totalRecords;
  }

  /**
   * Increments the number of processed records by one
   * and updates the values
   */
  public void incrementProcessedRecords() {
    processedRecords += 1;
    onProcessedRecordsUpdate(totalRecords, processedRecords);
  }

  private void onProcessedRecordsUpdate(long totalRecords, long processedRecords) {
    int percentComplete = (int) Math.ceil(100 * ((double) processedRecords / totalRecords));
    if (percentComplete == previousPercentComplete) {
      return;
    }

    previousPercentComplete = percentComplete;

    indicateStatus(percentComplete);
  }
  
  protected abstract void indicateStatus(int percentComplete);
}

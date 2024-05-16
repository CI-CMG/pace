package edu.colorado.cires.pace.packaging;

public abstract class ProgressIndicator {
  
  private long totalRecords = 0;
  private long processedRecords = 0;

  private int previousPercentComplete = 0;

  public void setTotalRecords(long totalRecords) {
    this.totalRecords = totalRecords;
  }

  public long getTotalRecords() {
    return totalRecords;
  }

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

    if (percentComplete == Integer.MAX_VALUE) {
      indicateStatus(0);
    } else {
      indicateStatus(percentComplete);
    }
  }
  
  protected abstract void indicateStatus(int percentComplete);
}

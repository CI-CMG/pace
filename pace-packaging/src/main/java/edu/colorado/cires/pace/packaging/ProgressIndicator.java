package edu.colorado.cires.pace.packaging;

public abstract class ProgressIndicator {
  
  private long totalRecords = 0;
  private long processedRecords = 0;

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
  
  protected abstract void onProcessedRecordsUpdate(long totalRecords, long processedRecords); 
}

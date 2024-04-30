package edu.colorado.cires.pace.packaging;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ProgressIndicatorTest {

  @Test
  void incrementProcessedRecords() {
    List<Long> processedRecordArgs = new ArrayList<>(0);
    List<Long> totalRecordsArgs = new ArrayList<>(0);
    
    ProgressIndicator progressIndicator = new ProgressIndicator() {
      @Override
      protected void onProcessedRecordsUpdate(long totalRecords, long processedRecords) {
        processedRecordArgs.add(processedRecords);
        totalRecordsArgs.add(totalRecords);
      }
    };
    
    int nInvocations = 30;
    progressIndicator.setTotalRecords(nInvocations);
    for (int i = 0; i < nInvocations; i++) {
      progressIndicator.incrementProcessedRecords();
    }
    
    assertEquals(nInvocations, totalRecordsArgs.get(0));
    for (int i = 1; i < processedRecordArgs.size(); i++) {
      assertEquals(processedRecordArgs.get(i - 1), processedRecordArgs.get(i) - 1);
      assertEquals(nInvocations, totalRecordsArgs.get(i));
    }
    
  }
}
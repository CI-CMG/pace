package edu.colorado.cires.pace.packaging;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ProgressIndicatorTest {

  @Test
  void incrementProcessedRecords() {
    List<Integer> percentCompleteArgs = new ArrayList<>(0);
    
    ProgressIndicator progressIndicator = new ProgressIndicator() {

      @Override
      protected void indicateStatus(int percentComplete) {
        percentCompleteArgs.add(percentComplete);
      }
    };
    
    int nInvocations = 30;
    progressIndicator.setTotalRecords(nInvocations);
    for (int i = 0; i < nInvocations; i++) {
      progressIndicator.incrementProcessedRecords();
    }
    
    assertEquals(nInvocations, percentCompleteArgs.size());
    
    for (int i = 0; i < percentCompleteArgs.size(); i++) {
      assertEquals((int) Math.ceil(100 * ((double) (i + 1) / nInvocations)), percentCompleteArgs.get(i));
    }
    
  }
}
package edu.colorado.cires.pace.gui;

import java.awt.Component;
import javax.swing.JScrollPane;

/**
 * ScrollPane extends JScrollPane and adds view component
 */
public class ScrollPane extends JScrollPane {
  
  private final Component component;

  /**
   * Creates scroll pane
   * @param view relevant view
   */
  public ScrollPane(Component view) {
    super(view);
    this.component = view;
  }

  /**
   * Returns view component
   * @return Component view
   */
  public Component getComponent() {
    return component;
  }
}

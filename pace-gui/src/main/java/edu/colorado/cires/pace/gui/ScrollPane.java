package edu.colorado.cires.pace.gui;

import java.awt.Component;
import javax.swing.JScrollPane;

public class ScrollPane extends JScrollPane {
  
  private final Component component;

  public ScrollPane(Component view) {
    super(view);
    this.component = view;
  }

  public Component getComponent() {
    return component;
  }
}

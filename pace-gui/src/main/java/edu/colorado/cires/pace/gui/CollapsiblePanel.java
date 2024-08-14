package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.getImageIcon;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class CollapsiblePanel<P extends JPanel> extends JPanel {
  
  private final String buttonTitle;
  private final P contentPanel;
  private final ImageIcon upIcon;
  private final ImageIcon downIcon;

  public CollapsiblePanel(String buttonTitle, P contentPanel) {
    this.buttonTitle = buttonTitle;
    this.contentPanel = contentPanel;
    this.upIcon = getImageIcon("keyboard_arrow_up_24dp_FILL0_wght400_GRAD0_opsz24.png", this.getClass());
    this.downIcon = getImageIcon("keyboard_arrow_down_24dp_FILL0_wght400_GRAD0_opsz24.png", this.getClass());

    setName(buttonTitle);
    addFields();
  }

  public P getContentPanel() {
    return contentPanel;
  }

  private void addFields() {
    setLayout(new GridBagLayout());
    setBorder(new EtchedBorder(EtchedBorder.RAISED));
    
    add(createButtonPanel(), configureLayout(c -> { c.gridx = 0; c.gridy = 0; c.weightx = 1; }));
    add(contentPanel, configureLayout(c -> { c.gridx = 0; c.gridy = 1; c.weightx = 1; }));
  }
  
  private JPanel createButtonPanel() {
    JLabel label = new JLabel(downIcon);
    
    JButton button = new JButton(buttonTitle);
    button.setOpaque(false);
    button.setBorderPainted(false);
    button.setFocusPainted(false);
    button.setContentAreaFilled(false);
    
    button.addActionListener(new ActionListener() {
      private boolean collapsed = true;
      
      @Override
      public void actionPerformed(ActionEvent e) {
        contentPanel.setVisible(collapsed);
        if (collapsed) {
          label.setIcon(upIcon);
        } else {
          label.setIcon(downIcon);
        }
        collapsed = !collapsed;
      }
    });
    
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(label, BorderLayout.WEST);
    panel.add(button, BorderLayout.CENTER);
    
    return panel;
  }
}

package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;
import static edu.colorado.cires.pace.gui.UIUtils.createSquareInsets;

import edu.colorado.cires.pace.data.translator.ChannelTranslator;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ChannelsForm extends JPanel {
  
  private static final int INSET_SIZE = 10;
  
  private final JPanel channelsPanel = new JPanel(new GridBagLayout());
  private final JButton addButton;

  public ChannelsForm(String[] headerOptions, List<ChannelTranslator> initialTranslators) {
    this.addButton = getAddButton(headerOptions);
    addFields();
    initializeFields(headerOptions, initialTranslators);
  }
  
  private void addFields() {
    setLayout(new GridBagLayout());
    add(channelsPanel, configureLayout(c -> {
      c.gridx = 0; c.gridy = 0; c.weightx = 1;
    }));
    add(addButton, configureLayout(c -> {
      c.gridx = 0; c.gridy = 1; c.weightx = 1;
    }));
    add(new JPanel(), configureLayout(c -> {
      c.gridx = 0; c.gridy = 2; c.weighty = 1;
    }));
  }
  
  private void initializeFields(String[] headerOptions, List<ChannelTranslator> initialTranslators) {
    if (initialTranslators != null) {
      initialTranslators.forEach(
          t -> addChannel(headerOptions, t)
      );
    }
  }
  
  private JButton getAddButton(String[] headerOptions) {
    JButton button = new JButton("Add");
    button.addActionListener(e -> addChannel(headerOptions, null));
    return button;
  }
  
  private void addChannel(String[] headerOptions, ChannelTranslator initialTranslator) {
    ChannelTranslatorForm channelTranslatorForm = new ChannelTranslatorForm(headerOptions, initialTranslator, f -> {
      channelsPanel.remove(f.getParent());
      revalidate();
    });
    
    CollapsiblePanel<ChannelTranslatorForm> collapsiblePanel = new CollapsiblePanel<>(
        String.format(
            "#%s", channelsPanel.getComponentCount() + 1
        ),
        channelTranslatorForm
    );
    collapsiblePanel.getContentPanel().setVisible(false);
    
    channelsPanel.add(collapsiblePanel, configureLayout(c -> {
      c.gridx = 0; c.gridy = channelsPanel.getComponentCount(); c.weightx = 1; c.insets = createSquareInsets(INSET_SIZE);
    }));
    revalidate();
  }
  
  public void updateHeaderOptions(String[] headerOptions) {
    Arrays.stream(channelsPanel.getComponents())
        .filter(p -> p instanceof CollapsiblePanel<?>)
        .map(p -> (CollapsiblePanel<?>) p)
        .map(p -> (ChannelTranslatorForm) p.getContentPanel())
        .forEach(p -> p.updateHeaderOptions(headerOptions));

    Arrays.stream(addButton.getActionListeners()).forEach(
        addButton::removeActionListener
    );
    addButton.addActionListener(e -> addChannel(headerOptions, null));
  }
  
  public List<ChannelTranslator> toTranslator() {
    return Arrays.stream(channelsPanel.getComponents())
        .filter(p -> p instanceof CollapsiblePanel<?>)
        .map(p -> (CollapsiblePanel<?>) p)
        .map(p -> (ChannelTranslatorForm) p.getContentPanel())
        .map(ChannelTranslatorForm::toTranslator)
        .toList();
  }
}

package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator.ChannelTranslator;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * ChannelsForm extends JPanel and adds fields relevant to channels
 */
public class ChannelsForm extends JPanel {

  private final JPanel channelsPanel = new JPanel(new GridBagLayout());
  private final JButton addButton;

  /**
   * Initializes a channels from
   * @param headerOptions headers to select from
   * @param initialTranslators base translator to add to
   */
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
    JButton button = new JButton("Add Channel");
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
            "Channel %s", channelsPanel.getComponentCount() + 1
        ),
        channelTranslatorForm
    );
    collapsiblePanel.getContentPanel().setVisible(false);
    
    channelsPanel.add(collapsiblePanel, configureLayout(c -> {
      c.gridx = 0; c.gridy = channelsPanel.getComponentCount(); c.weightx = 1;
    }));
    revalidate();
  }

  /**
   * Updates the selectable header options from dropdown menus
   * @param headerOptions to select from
   */
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

  /**
   * Provides the list of channel translators
   * @return ChannelTranslator list of held translators
   */
  public List<ChannelTranslator> toTranslator() {
    return Arrays.stream(channelsPanel.getComponents())
        .filter(p -> p instanceof CollapsiblePanel<?>)
        .map(p -> (CollapsiblePanel<?>) p)
        .map(p -> (ChannelTranslatorForm) p.getContentPanel())
        .map(ChannelTranslatorForm::toTranslator)
        .toList();
  }
}

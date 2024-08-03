package edu.colorado.cires.pace.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formdev.flatlaf.FlatIntelliJLaf;
import edu.colorado.cires.pace.utilities.ApplicationPropertyResolver;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.FontUIResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application extends JFrame {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

  public Application() {
  }

  public static void main(String[] args) {
    new Application().createGUI();
  }
  
  protected void createGUI() {
    FlatIntelliJLaf.setup();

    ObjectMapper objectMapper = SerializationUtils.createObjectMapper();

    java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
    while (keys.hasMoreElements()) {
      Object key = keys.nextElement();
      Object value = UIManager.get(key);
      if (value instanceof javax.swing.plaf.FontUIResource) {
        String font = ApplicationPropertyResolver.getPropertyValue("pace.gui.font", (s) -> s);
        Integer fontSize = ApplicationPropertyResolver.getPropertyValue("pace.gui.font-size", Integer::parseInt);

        UIManager.put(key, new FontUIResource(font, Font.PLAIN, fontSize == null ? 12 : fontSize));
      }
    }

    try {
      
      Dimension size = UIUtils.getPercentageOfWindowDimension(.75, .65);
      JFrame mainFrame = new JFrame();
      mainFrame.setIconImage(UIUtils.getImageIcon("pace.png", this.getClass()).getImage());
      ApplicationTabs applicationTabs = new ApplicationTabs(objectMapper);
      applicationTabs.init();
      mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      mainFrame.setContentPane(applicationTabs);
      mainFrame.setTitle("PACE");
      mainFrame.setSize(size);
      mainFrame.setPreferredSize(size);
      mainFrame.setLocationRelativeTo(null);
      
      mainFrame.setVisible(true);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    LOGGER.debug("Started GUI");
  }

}

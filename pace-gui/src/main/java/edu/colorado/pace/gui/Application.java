package edu.colorado.pace.gui;

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.IntelliJTheme;
import edu.colorado.cires.pace.utilities.ApplicationPropertyResolver;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.FontUIResource;

public class Application {
  
  public static void main(String[] args) {
    SwingUtilities.invokeLater(Application::createGUI);
  }
  
  private static void createGUI() {
    LafManager.install(new IntelliJTheme());

    ApplicationPropertyResolver propertyResolver = new ApplicationPropertyResolver();

    java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
    while (keys.hasMoreElements()) {
      Object key = keys.nextElement();
      Object value = UIManager.get(key);
      if (value instanceof javax.swing.plaf.FontUIResource) {
        String font = propertyResolver.getPropertyValue("pace-gui.font", (s) -> s);
        Integer fontSize = propertyResolver.getPropertyValue("pace-gui.font-size", Integer::parseInt);
        
        UIManager.put(key, new FontUIResource(font, Font.PLAIN, fontSize == null ? 12 : fontSize));
      }
    }
    
    try {
      MainUI ui = new MainUI();
      JPanel root = ui.getRootPanel();
      JFrame frame = new JFrame();
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      frame.setContentPane(root);
      frame.setTitle("PACE");
      frame.setSize(1000, 1000);
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}

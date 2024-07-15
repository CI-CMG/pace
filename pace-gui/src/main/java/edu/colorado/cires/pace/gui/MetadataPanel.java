package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.translator.Translator;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import edu.colorado.cires.pace.translator.converter.Converter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;

abstract class MetadataPanel<O extends ObjectWithUniqueField, T extends Translator> extends TranslatePanel<O, T> {

  private final Function<Object[], O> rowConversion;
  private final Function<O, Form<O>> formSupplier;

  public MetadataPanel(String name, CRUDRepository<O> repository, String[] headers, Function<O, Object[]> objectConversion,
      Class<O> clazz, 
      Function<Object[], O> rowConversion, Function<O, Form<O>> formSupplier, TranslatorRepository translatorRepository, Converter<T, O> converter,
      Class<T> translatorClazz) {
    super(name, repository, headers, objectConversion, clazz, translatorRepository, converter, translatorClazz);
    this.rowConversion = rowConversion;
    this.formSupplier = formSupplier;
  }

  @Override
  protected MouseAdapter getTableMouseAdapter() {
    return new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        JTable table = (JTable) e.getSource();
        Point point = e.getPoint();
        int row = table.rowAtPoint(point);
        if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
          List<Object> values = new ArrayList<>(0);
          for (int i = 0; i < headers.length; i++) {
            values.add(
                table.getModel().getValueAt(row, i)
            );
          }
          createFormDialog(rowConversion.apply(values.toArray()));
        }
      }
    };
  }

  @Override
  protected JPanel createControlPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    JButton translateButton = new JButton("Translate");
    panel.add(translateButton, BorderLayout.WEST);
    JButton createButton = new JButton("Create");
    panel.add(createButton, BorderLayout.EAST);

    createButton.addActionListener((e) -> createFormDialog(null));
    translateButton.addActionListener((e) -> {
      try {
        createTranslateForm();
      } catch (DatastoreException ex) {
        throw new RuntimeException(ex);
      }
    });

    return panel;
  }

  private void createFormDialog(O object) {
    JDialog dialog = new JDialog();
    Dimension size = getFormSize();
    dialog.setSize(size);
    dialog.setPreferredSize(size);
    dialog.setName("formDialog");
    dialog.setModal(true);
    dialog.setLocationRelativeTo(this);

    FormPanel<O> formPanel = new FormPanel<>(formSupplier.apply(object), repository, (s) -> {
      while (tableModel.getRowCount() > 0) {
        tableModel.removeRow(0);
      }
      s.forEach(o -> tableModel.addRow(objectConversion.apply(o)));
      dialog.dispose();
    }, object != null);

    dialog.add(formPanel);
    dialog.pack();
    dialog.setVisible(true);
  }
  
  private Dimension getFormSize() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = (int) (screenSize.width * 0.5);
    int height = (int) (screenSize.height * 0.4);
    return new Dimension(width, height);
  }
}

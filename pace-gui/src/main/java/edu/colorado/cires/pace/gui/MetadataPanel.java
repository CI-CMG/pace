package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.CSVTranslatorRepository;
import edu.colorado.cires.pace.repository.ExcelTranslatorRepository;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;

public class MetadataPanel<O extends ObjectWithUniqueField> extends TranslatorPanel<O> {

  private final Function<Object[], O> rowConversion;
  private final Function<O, Form<O>> formSupplier;

  public MetadataPanel(CRUDRepository<O> repository, String[] headers, Function<O, Object[]> objectConversion,
      ExcelTranslatorRepository excelTranslatorRepository, CSVTranslatorRepository csvTranslatorRepository, Class<O> clazz, 
      Function<Object[], O> rowConversion, Function<O, Form<O>> formSupplier, CRUDRepository<?>... dependencyRepositories) {
    super(repository, headers, objectConversion, excelTranslatorRepository, csvTranslatorRepository, clazz, dependencyRepositories);
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

    FormPanel<O> formPanel = new FormPanel<>(formSupplier.apply(object), repository, (s) -> {
      while (tableModel.getRowCount() > 0) {
        tableModel.removeRow(0);
      }
      s.forEach(o -> tableModel.addRow(objectConversion.apply(o)));
      dialog.dispose();
    });

    dialog.add(formPanel);
    dialog.pack();
    dialog.setVisible(true);
  }
}

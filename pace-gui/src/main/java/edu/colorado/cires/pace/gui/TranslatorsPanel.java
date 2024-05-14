package edu.colorado.cires.pace.gui;

import static edu.colorado.cires.pace.gui.UIUtils.configureLayout;

import edu.colorado.cires.pace.data.object.TabularTranslationField;
import edu.colorado.cires.pace.data.object.TabularTranslator;
import edu.colorado.cires.pace.repository.CRUDRepository;
import java.awt.GridBagLayout;
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

public class TranslatorsPanel<F extends TabularTranslationField, T extends TabularTranslator<F>> extends DataPanel<T> {

  private final Function<Object[], T> rowConversion;
  private final Function<T, Form<T>> formSupplier;

  public TranslatorsPanel(CRUDRepository<T> repository, String[] headers,
      Function<T, Object[]> objectConversion, Function<Object[], T> rowConversion, Function<T, Form<T>> formSupplier) {
    super(repository, headers, objectConversion);
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

  private void createFormDialog(T object) {
    JDialog dialog = new JDialog();

    FormPanel<T> formPanel = new FormPanel<>(formSupplier.apply(object), repository, (s) -> {
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

  @Override
  protected JPanel createControlPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    JButton createButton = new JButton("Create");
    panel.add(new JPanel(), configureLayout((c) -> { c.gridx = c.gridy = 0; c.weightx = 1; }));
    panel.add(createButton, configureLayout((c) -> { c.gridx = 1; c.gridy = 0; c.weightx = 0; }));

    createButton.addActionListener((e) -> createFormDialog(null));
    
    return panel;
  }
}

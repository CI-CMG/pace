package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.CRUDRepository;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DataPanel<O extends ObjectWithUniqueField> extends JPanel {
  
  private final TableModel tableModel;
  private final CRUDRepository<O> repository;
  private final Function<O, Object[]> objectConversion;
  private final Function<Object[], O> rowConversion;
  private final Function<O, Form<O>> formSupplier;

  public DataPanel(CRUDRepository<O> repository, String[] headers, Function<O, Object[]> objectConversion, Function<Object[], O> rowConversion, Function<O, Form<O>> formSupplier) {
    this.objectConversion = objectConversion;
    this.repository = repository;
    this.tableModel = new TableModel(
        null,
        headers
    );
    this.rowConversion = rowConversion;
    this.formSupplier = formSupplier;

    setLayout(new BorderLayout());
    
    add(createContentPanel(headers));
    
    loadData();
  }
  
  private JPanel createContentPanel(String[] headers) {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.add(new JScrollPane(createTable(headers)), BorderLayout.CENTER);
    panel.add(createControlPanel(), BorderLayout.SOUTH);
    return panel;
  }
  
  private JPanel createControlPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    JButton translateButton = new JButton("Translate");
    panel.add(translateButton, BorderLayout.WEST);
    JButton createButton = new JButton("Create");
    panel.add(createButton, BorderLayout.EAST);
    
    createButton.addActionListener((e) -> createFormDialog(null));
    
    return panel;
  }
  
  private void loadData() {
    try {
      repository.findAll().forEach(
          o -> tableModel.addRow(objectConversion.apply(o))
      );
    } catch (DatastoreException e) {
      throw new RuntimeException(e);
    }
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
  
  private JTable createTable(String[] headers) {
    JTable table = new JTable();
    table.setModel(tableModel);

    table.addMouseListener(new MouseAdapter() {
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
    });
    
    return table;
  }

  private static class TableModel extends DefaultTableModel {

    public TableModel(Object[][] data, Object[] columnNames) {
      super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
      return false;
    }
  }

}

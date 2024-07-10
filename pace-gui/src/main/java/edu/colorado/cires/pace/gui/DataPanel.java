package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public abstract class DataPanel<O extends ObjectWithUniqueField> extends JPanel {
  
  protected final DefaultTableModel tableModel;
  protected final CRUDRepository<O> repository;
  protected final Function<O, Object[]> objectConversion;
  protected final String[] headers;
  
  protected MouseAdapter getTableMouseAdapter() {
    return new MouseAdapter() {};
  }

  public DataPanel(
      String name,
      CRUDRepository<O> repository,
      String[] headers,
      Function<O, Object[]> objectConversion
  ) {
    setName(name);
    this.headers = headers;
    this.objectConversion = objectConversion;
    this.repository = repository;
    this.tableModel = createTableModel(headers);

    setLayout(new BorderLayout());
    
    add(createContentPanel());
    
    loadData();
  }
  
  protected DefaultTableModel createTableModel(String[] headers) {
    return new TableModel(
        null,
        headers
    );
  }
  
  private JPanel createContentPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.add(new JScrollPane(createTable()), BorderLayout.CENTER);
    panel.add(createControlPanel(), BorderLayout.SOUTH);
    return panel;
  }
  
  protected abstract JPanel createControlPanel();
  
  protected void loadData() {
    while (tableModel.getRowCount() > 0) {
      tableModel.removeRow(0);
    }
    
    try {
      repository.findAll().forEach(
          o -> tableModel.addRow(objectConversion.apply(o))
      );
    } catch (DatastoreException e) {
      throw new RuntimeException(e);
    }
  }
  
  protected List<String> getHiddenColumns() {
    return Collections.singletonList("UUID");
  }
  
  protected JTable createTable() {
    JTable table = new JTable();
    table.setName("dataTable");
    table.setModel(tableModel);
    table.setAutoCreateRowSorter(true);

    table.addMouseListener(getTableMouseAdapter());
    table.setCellSelectionEnabled(false);
    table.setRowSelectionAllowed(false);

    TableColumnModel columnModel = table.getColumnModel();
    getHiddenColumns().forEach(
        c -> columnModel.removeColumn(
            columnModel.getColumn(
                columnModel.getColumnIndex(c)
            )
        )
    );
    
    return table;
  }

  protected static class TableModel extends DefaultTableModel {

    public TableModel(Object[][] data, Object[] columnNames) {
      super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
      return false;
    }
  }

}

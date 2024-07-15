package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.translator.Translator;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import edu.colorado.cires.pace.repository.search.TranslatorSearchParameters;
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

public class TranslatorPanel extends DataPanel<Translator> {
  
  private final Function<Object[], Translator> rowConversion;
  private final Function<Translator, Form<Translator>> formSupplier;

  public TranslatorPanel(CRUDRepository<Translator> repository, String[] headers,
      Function<Translator, Object[]> objectConversion, Function<Object[], Translator> rowConversion,
      Function<Translator, Form<Translator>> formSupplier) {
    super("translatorsPanel", repository, headers, objectConversion);
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
    JButton createButton = new JButton("Create");
    panel.add(createButton, BorderLayout.EAST);

    createButton.addActionListener((e) -> createFormDialog(null));

    return panel;
  }

  @Override
  protected SearchParameters<Translator> getSearchParameters(List<String> uniqueFieldSearchTerms) {
    return TranslatorSearchParameters.builder()
        .names(uniqueFieldSearchTerms)
        .build();
  }

  private void createFormDialog(Translator object) {
    JDialog dialog = new JDialog();

    FormPanel<Translator> formPanel = new FormPanel<>(formSupplier.apply(object), repository, (s) -> {
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
}
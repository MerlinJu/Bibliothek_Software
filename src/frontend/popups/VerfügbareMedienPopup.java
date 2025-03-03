package frontend.popups;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import backend.Bibliothek;
import backend.Medientyp;
import backend.Medium;

// Design muss noch bearbeitet werden, aber sonst sollte es funktionieren

public class VerfügbareMedienPopup extends JDialog{

    private JTable medienTabelle;
    private DefaultTableModel tableModel;
    private JComboBox<String> filterDropdown;

    public VerfügbareMedienPopup(JFrame parent) {
        super(parent, "Verfügbare Medien anzeigen", true);
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        String[] filterOptions = {"Alle", "Buch", "Datenträger", "Diverse"};
        filterDropdown = new JComboBox<>(filterOptions);
        filterDropdown.setSelectedIndex(0); // Option "Alle" als default
        filterDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTableData();
            }
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Filter:"));
        topPanel.add(filterDropdown);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Titel", "Standplatz"}, 0);
        medienTabelle = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(medienTabelle);
        add(scrollPane, BorderLayout.CENTER);

        updateTableData();

        setSize(400, 300);
        setLocationRelativeTo(getParent()); // Zentriert das Popup relativ zum Hauptfenster
        setResizable(false);
    }

    private void updateTableData() {
        tableModel.setRowCount(0); // Tabelle clearen

        String selectedFilter = (String) filterDropdown.getSelectedItem();
        Medientyp filterTyp = null;
        if ("Buch".equals(selectedFilter)) {
            filterTyp = Medientyp.BUCH;
        } else if ("Datenträger".equals(selectedFilter)) {
            filterTyp = Medientyp.DATENTRÄGER;
        } else if ("Diverse".equals(selectedFilter)) {
            filterTyp = Medientyp.DIVERSE;
        }

        List<Medium> medienListe = Bibliothek.verfügbareMedien(filterTyp);
        for (Medium medium : medienListe) {
            tableModel.addRow(new Object[]{medium.titel, medium.standplatz});
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        VerfügbareMedienPopup popup = new VerfügbareMedienPopup(frame);
        popup.setVisible(true);
    }

}

package frontend.popups;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import backend.Bibliothek;
import backend.Medientyp;
import backend.Medium;

public class AusgelieheneMedienPopup extends JDialog{

    private DefaultTableModel tableModel;
    private JComboBox<String> filterDropdown;

    public AusgelieheneMedienPopup(JFrame parent) {
        super(parent, "Verfügbare Medien anzeigen", true);
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        String[] filterOptions = {"Alle", "Buch", "Datenträger", "Diverse"};
        filterDropdown = new JComboBox<>(filterOptions);
        filterDropdown.setSelectedIndex(0); // Option "Alle" als default
        filterDropdown.addActionListener(e -> updateTableData());

        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter:"));
        filterPanel.add(filterDropdown);

        JButton schließenButton = new JButton("Schließen");
        schließenButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(schließenButton);

        topPanel.add(filterPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Titel", "Ausleihe Datum", "Rückgabe Datum"}, 0);
        JTable medienTabelle = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(medienTabelle);
        add(scrollPane, BorderLayout.CENTER);

        updateTableData();

        setSize(450, 300);
        setLocationRelativeTo(getParent()); // Zentriert das Popup relativ zum Hauptfenster
        setResizable(false);
    }

    private void updateTableData() {
        tableModel.setRowCount(0); // Tabelle clearen

        String selectedFilter = (String) filterDropdown.getSelectedItem();
        Medientyp filterTyp = switch (selectedFilter) {
            case "Buch" -> Medientyp.BUCH;
            case "Datenträger" -> Medientyp.DATENTRÄGER;
            case "Diverse" -> Medientyp.DIVERSE;
            case null, default -> null;
        };

        List<Medium> medienListe = Bibliothek.ausgelieheneMedien(filterTyp);
        for (Medium medium : medienListe) {
            tableModel.addRow(new Object[]{medium.titel, medium.ausleihe_datum, medium.rueckgabe_datum});
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
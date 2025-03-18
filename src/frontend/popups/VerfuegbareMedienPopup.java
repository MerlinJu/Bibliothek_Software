package frontend.popups;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import backend.Bibliothek;
import backend.Medientyp;
import backend.Medium;

public class VerfuegbareMedienPopup extends JDialog{

    private JTable medienTabelle;
    private DefaultTableModel tableModel;
    private JComboBox<String> filterDropdown;
    private JButton schliessenButton;

    public VerfuegbareMedienPopup(JFrame parent) {
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

        schliessenButton = new JButton("Schließen");
        schliessenButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(schliessenButton);

        topPanel.add(filterPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Titel", "Standplatz"}, 0);
        medienTabelle = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(medienTabelle);
        add(scrollPane, BorderLayout.CENTER);

        updateTableData();

        setSize(450, 300);
        setLocationRelativeTo(getParent()); // Zentriert das Popup relativ zum Hauptfenster
        setResizable(true);
    }

    private void updateTableData() {
        tableModel.setRowCount(0); // Tabelle clearen

        String selectedFilter = (String) filterDropdown.getSelectedItem();
        Medientyp filterTyp = switch (selectedFilter) {
            case "Buch" -> Medientyp.BUCH;
            case "Datenträger" -> Medientyp.DATENTRAEGER;
            case "Diverse" -> Medientyp.DIVERSE;
            case null, default -> null;
        };

        List<Medium> medienListe = Bibliothek.verfuegbareMedien(filterTyp);
        for (Medium medium : medienListe) {
            tableModel.addRow(new Object[]{medium.getTitel(), medium.getStandplatz()});
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        VerfuegbareMedienPopup popup = new VerfuegbareMedienPopup(frame);
        popup.setVisible(true);
    }

}

package frontend.popups;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import backend.Bibliothek;
import backend.Medium;

public class ÜberfälligeMedienPopup extends JDialog{

    private DefaultTableModel tableModel;

    public ÜberfälligeMedienPopup(JFrame parent) {
        super(parent, "Überfällige Medien anzeigen", true);
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());

        JButton schließenButton = new JButton("Schließen");
        schließenButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(schließenButton);

        topPanel.add(buttonPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Titel", "Ausleihe Datum", "Rückgabe Datum", "Tag(e) überfällig"}, 0);
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

        List<Medium> medienListe = Bibliothek.ueberfaelligeMedien();
        for (Medium medium : medienListe) {
            tableModel.addRow(new Object[]{medium.titel, medium.ausleiheDatum, medium.rueckgabeDatum, medium.rueckgabeDatum.until(LocalDate.now(), ChronoUnit.DAYS)});
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

package frontend.popups;

import backend.Bibliothek;
import backend.Medium;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Vector;

public class MediumAusmusternPopup extends JDialog {
    private JComboBox<String> mediumDropdown;

    public MediumAusmusternPopup(JFrame parent) {
        super(parent, "Medium ausmustern", true);
        initializeUI();
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Medienliste abrufen und Titel extrahieren
        List<Medium> medienListe = Bibliothek.getMedienListe();
        Vector<String> medienTitelListe = new Vector<>();
        for (Medium medium : medienListe) {
            medienTitelListe.add(medium.titel);
        }

        mediumDropdown = new JComboBox<>(medienTitelListe);

        mainPanel.add(new JLabel("Medium auswählen:"));
        mainPanel.add(mediumDropdown);

        // Schließen-Button
        JButton schliessenButton = new JButton("Schließen");
        schliessenButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        schliessenButton.addActionListener(e -> dispose());
        mainPanel.add(schliessenButton);

        // Bestätigen-Button
        JButton ausmusternButton = new JButton("Ausmustern");
        ausmusternButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ausmusternButton.addActionListener((ActionEvent e) -> {
            String ausgewähltesMedium = (String) mediumDropdown.getSelectedItem();

            if (ausgewähltesMedium == null) {
                JOptionPane.showMessageDialog(null, "Bitte ein Medium auswählen!", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String message =  Bibliothek.vorhandenesMediumAusmustern(ausgewähltesMedium);

            JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });
        mainPanel.add(ausmusternButton);

        add(mainPanel);
        setSize(400, 150);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }
}


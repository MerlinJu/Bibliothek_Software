package frontend.popups;

import backend.Bibliothek;
import backend.Medium;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class StandplatzÄndernPopup extends JDialog {
    private JComboBox<String> mediumDropdown;


    public StandplatzÄndernPopup(JFrame parent) {
        super(parent, "Standplatz ändern", true);
        initializeUI();
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        List<String> medienListeTitel = new ArrayList<>();
        for (Medium medium : Bibliothek.verfuegbareMedien(null)) {
            medienListeTitel.add(medium.titel);
        }
        mediumDropdown = new JComboBox<>(new Vector<>(medienListeTitel));

        JTextField neuerStandplatzField = new JTextField();

        mainPanel.add(new JLabel("Medium:"));
        mainPanel.add(mediumDropdown);

        mainPanel.add(new JLabel("Neuer Standplatz:"));
        mainPanel.add(neuerStandplatzField);

        // schließen Button
        JButton schliessenButton = new JButton("Schließen");
        schliessenButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        schliessenButton.addActionListener(e -> dispose());
        mainPanel.add(schliessenButton);

        // Bestätigen Button
        JButton aendernButton = new JButton("Ändern");
        aendernButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        aendernButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ausgewähltesMediumTitel = (String) mediumDropdown.getSelectedItem();
                String neuerStandplatz = neuerStandplatzField.getText().trim();

                if (ausgewähltesMediumTitel == null || neuerStandplatz.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Bitte alle Felder ausfüllen!", "Fehler", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String message = Bibliothek.standplatzAendern(ausgewähltesMediumTitel, neuerStandplatz);

                JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });
        mainPanel.add(aendernButton);


        add(mainPanel);
        setSize(400, 200);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }
}

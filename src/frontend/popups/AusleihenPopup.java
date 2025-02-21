package frontend.popups;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AusleihenPopup extends JDialog {
    private JTextField ausleiheDatumField;
    private JLabel rueckgabeDatumLabel;

    public AusleihenPopup(JFrame parent) {
        super(parent, "Medium ausleihen", true);
        initializeUI();
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        // Eingabefeld für das Ausleihedatum
        mainPanel.add(new JLabel("Ausleihedatum (TT.MM.JJJJ)"));
        ausleiheDatumField = new JTextField();
        mainPanel.add(ausleiheDatumField);

        // Schaltfläche, um das Rückgabedatum zu berechnen
        JButton berechneRueckgabeButton = new JButton("Rückgabedatum berechnen");
        berechneRueckgabeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        berechneRueckgabeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                berechneRueckgabeDatum();
            }
        });
        mainPanel.add(berechneRueckgabeButton);

        // Label für die Anzeige des Rückgabedatums
        rueckgabeDatumLabel = new JLabel("Rückgabedatum: ");
        mainPanel.add(rueckgabeDatumLabel);

        // Schaltfläche, um das Popup zu schließen
        JButton schliessenButton = new JButton("Schließen");
        schliessenButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        schliessenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Schließt das Popup
            }
        });
        mainPanel.add(schliessenButton);

        // main panel zum dialog hinzufügen
        add(mainPanel);

        setSize(450, 200);
        setLocationRelativeTo(getParent()); // Zentriert das Popup relativ zum Hauptfenster
        setResizable(false);
    }

    private void berechneRueckgabeDatum() {
        String ausleiheDatum = ausleiheDatumField.getText();

        // Logik für rückgabewert

        String rueckgabeDatum = "31.12.2023"; // Beispielwert

        rueckgabeDatumLabel.setText("Rückgabedatum: "  + rueckgabeDatum);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        AusleihenPopup popup = new AusleihenPopup(frame);
        popup.setVisible(true);
    }

}

package frontend.popups;

import backend.Bibliothek;
import backend.Medium;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.time.LocalDate;
import java.util.Vector;

public class MediumAusleihenPopup extends JDialog {

    JLabel rueckgabeDatumLabel;

    public MediumAusleihenPopup(JFrame parent) {
        super(parent, "Medium ausleihen", true);
        initializeUI();
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<LocalDate> ausleiheDatumField;
        JComboBox<String> ausleiheTitelField;

        // Eingabefeld für das Ausleihedatum

        // alle date options
        Vector<LocalDate> dateOptions = new Vector<>();
        LocalDate today = LocalDate.now();
        for (int i = -7; i <= 7; i++) {  // Von 7 Tage vor heute bis 7 Tage nach heute
            dateOptions.add(today.plusDays(i));
        }

        mainPanel.add(new JLabel("Ausleihedatum (TT.MM.JJJJ)"));
        ausleiheDatumField = new JComboBox<>(dateOptions);
        ausleiheDatumField.setSelectedItem(today);
        mainPanel.add(ausleiheDatumField);

        Bibliothek.ladeMedienAusDatei();
        Vector<String> MediumOptions = new Vector<>();
        for (Medium medium : Bibliothek.verfügbareMedien(null)) {
            MediumOptions.add(medium.titel);
        }

        mainPanel.add(new JLabel("Titel des auszuleihenden Buches"));
        ausleiheTitelField = new JComboBox<>(MediumOptions);
        mainPanel.add(ausleiheTitelField);




        // Schaltfläche, um das Rückgabedatum zu berechnen
        JButton berechneRueckgabeButton = new JButton("Rückgabedatum berechnen");
        berechneRueckgabeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        berechneRueckgabeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                LocalDate ausleiheDatum = (LocalDate) ausleiheDatumField.getSelectedItem();
                if (ausleiheDatum != null) {
                    berechneRueckgabeDatum(ausleiheDatum);
                } else {
                    System.out.println("Kein gültiges Datum ausgewählt!");
                    JOptionPane.showMessageDialog(null, "Kein gültiges Datum ausgewählt!" ,"Fehler", JOptionPane.INFORMATION_MESSAGE);
                }

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

        JButton ausleihenButton = new JButton("Ausleihen");
        ausleihenButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ausleihenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    LocalDate ausleiheDatum = (LocalDate) ausleiheDatumField.getSelectedItem();
                    String ausleiheTitel = (String) ausleiheTitelField.getSelectedItem();

                    String message = Bibliothek.mediumAusleihen(ausleiheTitel, ausleiheDatum);

                    JOptionPane.showMessageDialog(null, message, "Meldung", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } catch (Exception exc) {
                    // Allgemeiner Block für alle anderen Fehler
                    JOptionPane.showMessageDialog(null, "Ein unbekannter Fehler ist aufgetreten: " + exc.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        mainPanel.add(ausleihenButton);



        // main panel zum dialog hinzufügen
        add(mainPanel);

        setSize(450, 200);
        setLocationRelativeTo(getParent()); // Zentriert das Popup relativ zum Hauptfenster
        setResizable(false);
    }

    private void berechneRueckgabeDatum(LocalDate ausleiheDatum) {
        LocalDate rueckgabeDatum = ausleiheDatum.plusDays(30); // Beispielwert

        rueckgabeDatumLabel.setText("Rückgabedatum: "  + rueckgabeDatum);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        MediumAusleihenPopup popup = new MediumAusleihenPopup(frame);
        popup.setVisible(true);
    }

}

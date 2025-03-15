package frontend.popups;

import backend.Bibliothek;
import backend.Medium;
import backend.Status;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class MediumZurückgebenPopup extends JDialog {
    private JComboBox<String> ausgelieheneMedienDropdown;
    private JLabel rückgabeDatumLabel;
    private JLabel mediumAuswahl;
    private JButton zurückgebenButton, schließeButton;
    private JLabel ueberfaelligLabel;
    private JLabel ueberfaelligIn;


    public MediumZurückgebenPopup(JFrame parent) {
        super(parent, "Medium zurückgeben", true);
        initializeUI();
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mediumAuswahl = new JLabel("Medium:");
        mainPanel.add(mediumAuswahl);

        Map<String, Medium> mediumMap = new HashMap<>();
        List<Medium> AusgelieheneMedien = Bibliothek.ausgelieheneMedien(null);
        List<String> AusgelieheneMedienTitel = new ArrayList<>();

        for (Medium medium : AusgelieheneMedien) {
            AusgelieheneMedienTitel.add(medium.titel);
            mediumMap.put(medium.titel, medium);
        }

        ausgelieheneMedienDropdown = new JComboBox<>(new Vector<>(AusgelieheneMedienTitel));
        mainPanel.add(ausgelieheneMedienDropdown);

        ueberfaelligLabel = new JLabel("Überfällig:");
        mainPanel.add(ueberfaelligLabel);
        ueberfaelligIn = new JLabel("");
        mainPanel.add(ueberfaelligIn);

        // Initialen Zustand setzen
        String initialTitel = (String) ausgelieheneMedienDropdown.getSelectedItem();
        Medium initialMedium = mediumMap.get(initialTitel);
        if (initialMedium != null) {
            ueberfaelligIn.setText(Bibliothek.istMediumUeberfaellig(initialMedium));
        } else {
            ueberfaelligIn.setText("Nicht überfällig");
        }

        JLabel neuerStandplatzLabel = new JLabel("neuer Standplatz:");
        mainPanel.add(neuerStandplatzLabel);

        JTextField neuerStandplatz = new JTextField();
        mainPanel.add(neuerStandplatz);

        ausgelieheneMedienDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String gewähltesMediumTitel = (String) ausgelieheneMedienDropdown.getSelectedItem();
                Medium medium = mediumMap.get(gewähltesMediumTitel); // Medium abrufen von der Hash Map, mithilfe von dem Titel der an das Medium Objekt geknüpft ist

                if (medium != null) {
                    ueberfaelligIn.setText(Bibliothek.istMediumUeberfaellig(medium));
                } else {
                    ueberfaelligIn.setText("Nicht überfällig");
                }

            }
        });

        // Buttons

        JButton schließenButton = new JButton("Schließen");
        schließenButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        schließenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        mainPanel.add(schließenButton);

        zurückgebenButton = new JButton("Zurückgeben");
        zurückgebenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String gewähltesMediumTitel = (String) ausgelieheneMedienDropdown.getSelectedItem();
                    String standplatz = neuerStandplatz.getText().trim();

                    if(mediumMap.get(gewähltesMediumTitel).status != Status.AUSGELIEHEN_VORGEMERKT) {
                        if (standplatz.isEmpty()) {
                            throw new IllegalArgumentException("Alle Felder müssen ausgefüllt sein!");
                        } else if (Bibliothek.standplatzUngueltig(standplatz)) {
                            throw new IllegalArgumentException("Das Format des Standplatzes ist ungültig!");
                        }
                    }

                    String result = Bibliothek.mediumZurueckgeben(gewähltesMediumTitel, standplatz);

                    JOptionPane.showMessageDialog(null, result, "Meldung", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } catch (StringIndexOutOfBoundsException SIOOBe) {
                    // Beispiel für eine Ausnahme, die beim Überschreiten der maximalen Textlänge ausgelöst wird
                    JOptionPane.showMessageDialog(null, "Der eingegebene Text ist zu lang!", "Fehler", JOptionPane.ERROR_MESSAGE);
                } catch (Exception exc) {
                    // Allgemeiner Block für alle anderen Fehler
                    JOptionPane.showMessageDialog(null, "Ein unbekannter Fehler ist aufgetreten: " + exc.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        mainPanel.add(zurückgebenButton);


        add(mainPanel);
        setSize(500, 200);
        setLocationRelativeTo(getParent()); // Zentriert das Popup relativ zum Hauptfenster
        setResizable(false);

    }

}

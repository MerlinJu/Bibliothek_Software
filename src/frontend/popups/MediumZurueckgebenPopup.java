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

public class MediumZurueckgebenPopup extends JDialog {
    private JComboBox<String> ausgelieheneMedienDropdown;
    private JLabel mediumAuswahl;
    private JButton zurueckgebenButton;
    private JLabel ueberfaelligLabel;
    private JLabel ueberfaelligIn;


    public MediumZurueckgebenPopup(JFrame parent) {
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
            AusgelieheneMedienTitel.add(medium.getTitel());
            mediumMap.put(medium.getTitel(), medium);
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

        JLabel neuerStandplatzLabel = new JLabel("Neuer Standplatz:");
        mainPanel.add(neuerStandplatzLabel);

        JTextField neuerStandplatz = new JTextField();
        mainPanel.add(neuerStandplatz);

        ausgelieheneMedienDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String gewaehltesMediumTitel = (String) ausgelieheneMedienDropdown.getSelectedItem();
                Medium medium = mediumMap.get(gewaehltesMediumTitel); // Medium abrufen von der Hash Map, mithilfe von dem Titel der an das Medium Objekt geknüpft ist

                if (medium != null) {
                    ueberfaelligIn.setText(Bibliothek.istMediumUeberfaellig(medium));
                } else {
                    ueberfaelligIn.setText("Nicht überfällig");
                }

            }
        });

        // Buttons

        JButton schliessenButton = new JButton("Schließen");
        schliessenButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        schliessenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        mainPanel.add(schliessenButton);

        zurueckgebenButton = new JButton("Zurückgeben");
        zurueckgebenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String gewaehltesMediumTitel = (String) ausgelieheneMedienDropdown.getSelectedItem();
                    String standplatz = neuerStandplatz.getText().trim();

                    // Überprüft, ob der Standplatz im gültigen Format ist (entfällt bei vorgemerkten Medien)
                    if(mediumMap.get(gewaehltesMediumTitel).getStatus() != Status.AUSGELIEHEN_VORGEMERKT && Bibliothek.standplatzUngueltig(standplatz)) {
                        JOptionPane.showMessageDialog(null, "Das Format des Standplatzes ist ungültig!", "Fehler", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String result = Bibliothek.mediumZurueckgeben(gewaehltesMediumTitel, standplatz);

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
        mainPanel.add(zurueckgebenButton);


        add(mainPanel);
        setSize(500, 200);
        setLocationRelativeTo(getParent()); // Zentriert das Popup relativ zum Hauptfenster
        setResizable(false);

    }

}

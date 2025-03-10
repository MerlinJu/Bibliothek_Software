package frontend.popups;

import backend.Bibliothek;
import backend.Medium;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.AreaAveragingScaleFilter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MediumZurückgebenPopup extends JDialog {
    private JComboBox<String> ausgelieheneMedienDropdown;
    private JLabel rückgabeDatumLabel;
    private JLabel mediumAuswahl;
    private JButton zurückgebenButton, schließeButton;


    public MediumZurückgebenPopup(JFrame parent) {
        super(parent, "Medium zurückgeben", true);
        initializeUI();
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mediumAuswahl = new JLabel("Medium:");
        mainPanel.add(mediumAuswahl);

        List<Medium> AusgelieheneMedien = Bibliothek.ausgelieheneMedien(null);
        List<String> AusgelieheneMedienTitel = new ArrayList<>();
        for (Medium medium : AusgelieheneMedien) {
            AusgelieheneMedienTitel.add(medium.titel);
        }

        ausgelieheneMedienDropdown = new JComboBox<>(new Vector<>(AusgelieheneMedienTitel));
        mainPanel.add(ausgelieheneMedienDropdown);

        JLabel neuerStandplatzLabel = new JLabel("neuer Standplatz:");
        mainPanel.add(neuerStandplatzLabel);

        JTextField neuerStandplatz = new JTextField();
        mainPanel.add(neuerStandplatz);

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
                    String mediumTitel = (String) ausgelieheneMedienDropdown.getSelectedItem();
                    String standplatz = neuerStandplatz.getText().trim();

                    if (standplatz.isEmpty()) {
                        throw new IllegalArgumentException("Alle Felder müssen ausgefüllt sein!");
                    }

                    String result = Bibliothek.mediumZurückgeben(mediumTitel, standplatz);

                    JOptionPane.showMessageDialog(null, result, "Meldung", JOptionPane.INFORMATION_MESSAGE);
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
        setSize(450, 200);
        setLocationRelativeTo(getParent()); // Zentriert das Popup relativ zum Hauptfenster
        setResizable(false);

    }

}

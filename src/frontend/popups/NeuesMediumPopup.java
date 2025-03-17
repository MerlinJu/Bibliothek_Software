package frontend.popups;

import backend.Bibliothek;
import backend.Medientyp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NeuesMediumPopup extends JDialog {

    public NeuesMediumPopup(JFrame parent) {
        super(parent, "Neues Medium hinzufügen", true);
        initializeUI();
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField neuesMediumTitel = new JTextField();
        JTextField neuesMediumAutor = new JTextField();
        JTextField neuesMediumStandplatz = new JTextField();
        JComboBox<Medientyp> neuesMediumTyp;

        // Eingabefelder fuer alle 3 Werte die das neue Medium braucht
        JTextField[] textFields = {neuesMediumTitel, neuesMediumAutor, neuesMediumStandplatz};
        String[] labels = {"Titel von neuem Medium:", "Autor:", "Standplatz:"};

        // loopt durch alle 3 Input und Label Objekte und fuegt sie dem mainPanel hinzu
        for (int i = 0; i < labels.length; i++) {
            mainPanel.add(new JLabel(labels[i]));
            mainPanel.add(textFields[i]);
        }

        String[] medienTypen = {"Buch", "Datenträger", "Diverse"};

        // Medientyp Select field
        mainPanel.add(new JLabel("Medientyp:"));
        neuesMediumTyp = new JComboBox<>(Medientyp.values());
        mainPanel.add(neuesMediumTyp);


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

        // Button um Medium hinzuzufügen
        JButton HinzufuegenButton = new JButton("Hinzufügen");
        HinzufuegenButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        HinzufuegenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String titel = neuesMediumTitel.getText().trim();
                    String autor = neuesMediumAutor.getText().trim();
                    String standplatz = neuesMediumStandplatz.getText().trim();
                    Medientyp typ = (Medientyp) neuesMediumTyp.getSelectedItem();

                    // Überpruefen, ob alle Felder ausgefüllt sind
                    if (titel.isEmpty() || autor.isEmpty() || standplatz.isEmpty()) {
                        throw new IllegalArgumentException("Alle Felder müssen ausgefüllt sein!");
                    }

                    // Hier wird das neue Medium hinzugefügt und ein String mit Error oder Erfolg zurückgegeben
                    String result = Bibliothek.neuesMediumHinzufuegen(titel, autor, standplatz, typ);

                    // zeigt erfolg oder error als popup an
                    JOptionPane.showMessageDialog(null, result, "Meldung", JOptionPane.INFORMATION_MESSAGE);

                } catch (StringIndexOutOfBoundsException SIOOBe) {
                    // Beispiel für eine Ausnahme, die beim Überschreiten der maximalen Textlänge ausgelöst wird
                    JOptionPane.showMessageDialog(null, "Der eingegebene Text ist zu lang!", "Fehler", JOptionPane.ERROR_MESSAGE);
                } catch (Exception exc) {
                    // Allgemeiner Block für alle anderen Fehler
                    JOptionPane.showMessageDialog(null, "Ein unbekannter Fehler ist aufgetreten: " + exc.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                }
                dispose();

            }
        });
        mainPanel.add(HinzufuegenButton);



        add(mainPanel);

        setSize(450, 300);
        setLocationRelativeTo(getParent()); // Zentriert das Popup relativ zum Hauptfenster
        setResizable(false);
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        NeuesMediumPopup popup = new NeuesMediumPopup(frame);
        popup.setVisible(true);
    }


}

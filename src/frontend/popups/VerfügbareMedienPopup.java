package frontend.popups;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import backend.Bibliothek;
import backend.Medientyp;

// Design muss noch bearbeitet werden, aber sonst sollte es funktionieren

public class VerfügbareMedienPopup extends JDialog{

    private JLabel verfügbareMedien;

    public VerfügbareMedienPopup(JFrame parent) {
        super(parent, "Verfügbare Medien anzeigen", true);
        initializeUI();
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new FlowLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Label, in dem die gefilterten Medien angezeigt werden
        verfügbareMedien = new JLabel();
        verfügbareMedien.setText(Bibliothek.verfügbareMedien(null)); // Beim Öffnen des Popups werden standardmäßig alle Medien im Bestand aufgeführt
        mainPanel.add(verfügbareMedien, BorderLayout.WEST);

        // Button für den Alle-Filter
        JButton filternNachAlle = new JButton("Filter: Alle");
        filternNachAlle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        filternNachAlle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verfügbareMedien.setText(Bibliothek.verfügbareMedien(null));
            }
        });
        mainPanel.add(filternNachAlle);

        // Button für den Buch-Filter
        JButton filternNachBuch = new JButton("Filter: Buch");
        filternNachBuch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        filternNachBuch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verfügbareMedien.setText(Bibliothek.verfügbareMedien(Medientyp.BUCH));
            }
        });
        mainPanel.add(filternNachBuch);

        // Button für den Datenträger-Filter
        JButton filternNachDatenträger = new JButton("Filter: Datenträger");
        filternNachDatenträger.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        filternNachDatenträger.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verfügbareMedien.setText(Bibliothek.verfügbareMedien(Medientyp.DATENTRÄGER));
            }
        });
        mainPanel.add(filternNachDatenträger);

        // Button für den Diverse-Filter
        JButton filternNachDiverse = new JButton("Filter: Diverse");
        filternNachDiverse.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        filternNachDiverse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verfügbareMedien.setText(Bibliothek.verfügbareMedien(Medientyp.DIVERSE));
            }
        });
        mainPanel.add(filternNachDiverse);

        // main panel zum dialog hinzufügen
        add(mainPanel);
        pack();

        setLocationRelativeTo(getParent()); // Zentriert das Popup relativ zum Hauptfenster
        setResizable(false);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        VerfügbareMedienPopup popup = new VerfügbareMedienPopup(frame);
        popup.setVisible(true);
    }

}

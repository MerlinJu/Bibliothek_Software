package frontend.popups;

import backend.Bibliothek;
import backend.Medientyp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Design muss noch bearbeitet werden, aber sonst sollte es funktionieren

public class ÜberfälligeMedienPopup extends JDialog{

    private JLabel überfälligeMedien;

    public ÜberfälligeMedienPopup(JFrame parent) {
        super(parent, "Überfällige Medien anzeigen", true);
        initializeUI();
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new FlowLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Label, in dem die gefilterten Medien angezeigt werden
        überfälligeMedien = new JLabel();
        überfälligeMedien.setText(Bibliothek.überfälligeMedien());
        mainPanel.add(überfälligeMedien, BorderLayout.WEST);

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

        ÜberfälligeMedienPopup popup = new ÜberfälligeMedienPopup(frame);
        popup.setVisible(true);
    }

}

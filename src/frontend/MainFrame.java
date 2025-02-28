package frontend;

import backend.Bibliothek;
import frontend.popups.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {



    public MainFrame() {
        initilaizeUI();
    }

    private void initilaizeUI() {
        // FENSTEREINSTELLUNGEN
        setTitle("Bibliothek");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        //hauptpanel mit Borderlayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)); // Abstände zwischen Komponenten
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Rand um das Hauptpanel

        // Menüleiste erstellen
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);

        // Button Panel erstellen
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false); // User kann Text nicht bearbeiten
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Schriftart anpassen
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Ausgabe")); // Rahmen mit Titel
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        /** JavaDoc Kommentare */

        add(mainPanel);
        Bibliothek.ladeMedien();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Datei Menü
        JMenu fileMenu = new JMenu("Datei");
        fileMenu.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Schriftart anpassen
        JMenuItem exitMenuItem = new JMenuItem("Beenden");
        exitMenuItem.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Schriftart anpassen
        exitMenuItem.addActionListener(e -> System.exit(0)); // Beendet das Programm
        fileMenu.add(exitMenuItem);

        // Hilfe-Menü
        JMenu helpMenu = new JMenu("Hilfe");
        helpMenu.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Schriftart anpassen
        JMenuItem aboutMenuItem = new JMenuItem("Über");
        aboutMenuItem.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Schriftart anpassen
        aboutMenuItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Bibliotheksverwaltung v1.0"));
        helpMenu.add(aboutMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10)); // Grid Layout

        // Buttons für die verschiedenen Funktionen
        JButton addMediumButton = createStyledButton("Neues Medium hinzufügen");
        addMediumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NeuesMediumPopup popup = new NeuesMediumPopup(MainFrame.this);
                popup.setVisible(true);
            }
        });

        JButton lendMediumButton = createStyledButton("Medium ausleihen");
        lendMediumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AusleihenPopup popup = new AusleihenPopup(MainFrame.this);
                popup.setVisible(true);
            }
        });

        JButton returnMediumButton = createStyledButton("Medium zurückgeben");

        JButton listAvailableMediaButton = createStyledButton("Verfügbare Medien anzeigen");
        listAvailableMediaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VerfügbareMedienPopup popup = new VerfügbareMedienPopup(MainFrame.this);
                popup.setVisible(true);
            }
        });

        JButton listLentMediaButton = createStyledButton("Ausgeliehene Medien anzeigen");
        listLentMediaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AusgelieheneMedienPopup popup = new AusgelieheneMedienPopup(MainFrame.this);
                popup.setVisible(true);
            }
        });

        JButton listOverdueMediaButton = createStyledButton("Überfällige Medien anzeigen");
        listOverdueMediaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ÜberfälligeMedienPopup popup = new ÜberfälligeMedienPopup(MainFrame.this);
                popup.setVisible(true);
            }
        });

        JButton changeLocationButton = createStyledButton("Standplatz ändern");

        JButton removeMediumButton = createStyledButton("Medium ausmustern");

        // Buttons zum Panel hinzufügen
        panel.add(addMediumButton);
        panel.add(lendMediumButton);
        panel.add(returnMediumButton);
        panel.add(listAvailableMediaButton);
        panel.add(listLentMediaButton);
        panel.add(listOverdueMediaButton);
        panel.add(changeLocationButton);
        panel.add(removeMediumButton);

        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14)); // Schriftart anpassen
        button.setFocusPainted(false); // Entfernt den Fokus-Rahmen

        button.setForeground(Color.BLACK); // Textfarbe
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }

}

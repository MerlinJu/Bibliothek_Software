package frontend;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {



    public MainFrame() {
        initilaizeUI();
    }

    private void initilaizeUI() {
        // FENSTEREINSTELLUNGEN
        setTitle("Bibliothek");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        //hauptpanel mit Borderlayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Menüleiste erstellen
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);

        // Button Panel erstellen
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false); // User kann Text nicht bearbeiten
        JScrollPane scrollPane = new JScrollPane(outputArea);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        /** JavaDoc Kommentare */

        add(mainPanel);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Datei Menü
        JMenu fileMenu = new JMenu("Datei");
        JMenuItem exitMenuItem = new JMenuItem("Beenden");
        exitMenuItem.addActionListener(e -> System.exit(0)); // Beendet das Programm
        fileMenu.add(exitMenuItem);

        // Hilfe-Menü
        JMenu helpMenu = new JMenu("Hilfe");
        JMenuItem aboutMenuItem = new JMenuItem("Über");
        aboutMenuItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Bibliotheksverwaltung v1.0"));
        helpMenu.add(aboutMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10)); // Grid Layout

        // Buttons für die verschiedenen Funktionen
        JButton addMediumButton = new JButton("Neues Medium hinzufügen");
        JButton lendMediumButton = new JButton("Medium ausleihen");
        JButton returnMediumButton = new JButton("Medium zurückgeben");
        JButton listAvailableMediaButton = new JButton("Verfügbare Medien anzeigen");
        JButton listLentMediaButton = new JButton("Ausgeliehene Medien anzeigen");
        JButton listOverdueMediaButton = new JButton("Überfällige Medien anzeigen");
        JButton changeLocationButton = new JButton("Standplatz ändern");
        JButton removeMediumButton = new JButton("Medium ausmustern");

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



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }

}

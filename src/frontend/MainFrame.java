package frontend;

import backend.Bibliothek;
import frontend.popups.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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

        add(mainPanel);
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

        ImageIcon logoIcon = null;
        try {
            File file = new File("src/media/logo.jpeg");
            Image image = ImageIO.read(file);
            Image scaledImage = image.getScaledInstance(140, 100, Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.out.println("Bild konnte nicht geladen werden: " + e.getMessage());
        }
        final ImageIcon finalLogoIcon = logoIcon;

        aboutMenuItem.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        null,
                        "Bibliotheksverwaltung v1.0",
                        "Über",
                        JOptionPane.INFORMATION_MESSAGE,
                        finalLogoIcon
                )
        );

        helpMenu.add(aboutMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10)); // Grid Layout

        // Buttons für die verschiedenen Funktionen
        JButton mediumHinzufuegenButton = createStyledButton("Neues Medium hinzufügen");
        mediumHinzufuegenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NeuesMediumPopup popup = new NeuesMediumPopup(MainFrame.this);
                popup.setVisible(true);
            }
        });

        JButton mediumAusleihenButton = createStyledButton("Medium ausleihen");
        mediumAusleihenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MediumAusleihenPopup popup = new MediumAusleihenPopup(MainFrame.this);
                popup.setVisible(true);
            }
        });

        JButton mediumZurueckgebenButton = createStyledButton("Medium zurückgeben");
        mediumZurueckgebenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MediumZurueckgebenPopup popup = new MediumZurueckgebenPopup(MainFrame.this);
                popup.setVisible(true);
            }
        });

        JButton verfuegbareMedienButton = createStyledButton("Verfügbare Medien anzeigen");
        verfuegbareMedienButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VerfuegbareMedienPopup popup = new VerfuegbareMedienPopup(MainFrame.this);
                popup.setVisible(true);
            }
        });

        JButton ausgelieheneMedienButton = createStyledButton("Ausgeliehene Medien anzeigen");
        ausgelieheneMedienButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AusgelieheneMedienPopup popup = new AusgelieheneMedienPopup(MainFrame.this);
                popup.setVisible(true);
            }
        });

        JButton ueberfaelligeMedienButton = createStyledButton("Überfällige Medien anzeigen");
        ueberfaelligeMedienButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UeberfaelligeMedienPopup popup = new UeberfaelligeMedienPopup(MainFrame.this);
                popup.setVisible(true);
            }
        });

        JButton standplatzAendernButton = createStyledButton("Standplatz ändern");
        standplatzAendernButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StandplatzAendernPopup popup = new StandplatzAendernPopup(MainFrame.this);
                popup.setVisible(true);
            }
        });

        JButton mediumAusmusternButton = createStyledButton("Medium ausmustern");
        mediumAusmusternButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MediumAusmusternPopup popup = new MediumAusmusternPopup(MainFrame.this);
                popup.setVisible(true);
            }
        });

        // Buttons zum Panel hinzufügen
        panel.add(mediumHinzufuegenButton);
        panel.add(mediumAusleihenButton);
        panel.add(mediumZurueckgebenButton);
        panel.add(verfuegbareMedienButton);
        panel.add(ausgelieheneMedienButton);
        panel.add(ueberfaelligeMedienButton);
        panel.add(standplatzAendernButton);
        panel.add(mediumAusmusternButton);

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
        Bibliothek.ladeMedienAusDatei();

        // Beim Beenden des Programms wird der Inhalt der medienListe in die medien.txt Datei überschrieben
        Runtime.getRuntime().addShutdownHook(new Thread(Bibliothek::schreibeMedienInDatei));

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }

}

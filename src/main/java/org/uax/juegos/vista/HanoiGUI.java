package org.uax.juegos.vista;

import javax.swing.*;
import java.awt.*;

public class HanoiGUI extends JFrame {
    public JPanel panelTorres;
    public JTextField discosInput;
    public JTextArea movimientosArea;
    public JTextArea historialArea;
    public JButton iniciarButton;
    public JButton volverButton;
    public JButton guardarButton;
    public JButton mostrarHistorialButton; // Nuevo botón


    public HanoiGUI() {
        setTitle("Torres de Hanoi");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        discosInput = new JTextField("3", 5);
        iniciarButton = new JButton("Iniciar");
        volverButton = new JButton("Volver");
        guardarButton = new JButton("Guardar");
        mostrarHistorialButton = new JButton("Historial");
        movimientosArea = new JTextArea();
        movimientosArea.setEditable(false);

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Número de discos:"));
        topPanel.add(discosInput);
        topPanel.add(iniciarButton);
        topPanel.add(guardarButton);
        topPanel.add(mostrarHistorialButton);
        add(topPanel, BorderLayout.NORTH);

        panelTorres = new JPanel(new GridLayout(1, 3));
        add(panelTorres, BorderLayout.CENTER);

        for (int i = 0; i < 3; i++) {
            JPanel torre = new JPanel();
            torre.setLayout(new BoxLayout(torre, BoxLayout.Y_AXIS));
            torre.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            torre.setBackground(Color.LIGHT_GRAY);
            torre.add(Box.createVerticalGlue());

            JLabel etiqueta = new JLabel(String.valueOf((char) ('A' + i)), JLabel.CENTER);
            etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);
            torre.add(etiqueta);

            panelTorres.add(torre);
        }

        // Panel central para las torres
        panelTorres = new JPanel(new GridLayout(1, 3));
        add(panelTorres, BorderLayout.CENTER);

        for (int i = 0; i < 3; i++) {
            JPanel torre = new JPanel();
            torre.setLayout(new BoxLayout(torre, BoxLayout.Y_AXIS));
            torre.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            torre.setBackground(Color.LIGHT_GRAY);
            torre.add(Box.createVerticalGlue());

            JLabel etiqueta = new JLabel(String.valueOf((char) ('A' + i)), JLabel.CENTER);
            etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);
            torre.add(etiqueta);

            panelTorres.add(torre);
        }

        // Panel lateral con movimientos, botón guardar y área de historial
        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));

        movimientosArea = new JTextArea();
        movimientosArea.setEditable(false);
        JScrollPane movimientosScroll = new JScrollPane(movimientosArea);
        movimientosScroll.setPreferredSize(new Dimension(200, 250));

        historialArea = new JTextArea();
        historialArea.setEditable(false);
        JScrollPane historialScroll = new JScrollPane(historialArea);
        historialScroll.setPreferredSize(new Dimension(200, 150));

        eastPanel.add(new JLabel("Movimientos:"));
        eastPanel.add(movimientosScroll);
        eastPanel.add(Box.createVerticalStrut(10));
        eastPanel.add(Box.createVerticalStrut(10));
        eastPanel.add(new JLabel("Historial:"));
        eastPanel.add(historialScroll);

        add(eastPanel, BorderLayout.EAST);
        add(volverButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void mostrarMovimiento(int origen, int destino, int disco) {
        movimientosArea.append("Mover disco " + disco + " de torre " + (char) ('A' + origen) + " a torre " + (char) ('A' + destino) + "\n");
    }

}

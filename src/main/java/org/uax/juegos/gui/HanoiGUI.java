package org.uax.juegos.gui;

import javax.swing.*;
import java.awt.*;

public class HanoiGUI extends JFrame {
    public JPanel panelTorres;
    public JTextField discosInput;
    public JTextArea movimientosArea;
    public JButton iniciarButton;
    public JButton volverButton;

    public HanoiGUI() {
        setTitle("Torres de Hanoi");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        discosInput = new JTextField("3", 5);
        iniciarButton = new JButton("Iniciar");
        volverButton = new JButton("Volver");
        movimientosArea = new JTextArea();
        movimientosArea.setEditable(false);

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Número de discos:"));
        topPanel.add(discosInput);
        topPanel.add(iniciarButton);
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

        JScrollPane scroll = new JScrollPane(movimientosArea);
        scroll.setPreferredSize(new Dimension(200, 0));
        add(scroll, BorderLayout.EAST);
        add(volverButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void mostrarMovimiento(int origen, int destino) {
        movimientosArea.append("Mover disco de torre " + (char) ('A' + origen) + " a torre " + (char) ('A' + destino) + "\n");
    }
}

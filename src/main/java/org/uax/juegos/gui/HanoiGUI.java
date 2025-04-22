package org.uax.juegos.gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HanoiGUI extends JFrame {
    public JPanel panelTorres;
    public JTextField discosInput;
    public JTextArea movimientosArea;
    public JButton iniciarButton;
    public JButton volverButton;
    public JLabel[] etiquetasTorres;

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

        etiquetasTorres = new JLabel[3];
        for (int i = 0; i < 3; i++) {
            JPanel torre = new JPanel();
            torre.setLayout(new BoxLayout(torre, BoxLayout.Y_AXIS));
            torre.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            torre.setBackground(Color.LIGHT_GRAY);
            torre.add(Box.createVerticalGlue());
            panelTorres.add(torre);

            // Etiqueta de torre (A, B, C)
            JLabel etiquetaTorre = new JLabel(String.valueOf((char) ('A' + i)), JLabel.CENTER);
            etiquetaTorre.setAlignmentX(Component.CENTER_ALIGNMENT);
            torre.add(etiquetaTorre);
            etiquetasTorres[i] = etiquetaTorre;
        }

        JScrollPane scroll = new JScrollPane(movimientosArea);
        scroll.setPreferredSize(new Dimension(200, 0));
        add(scroll, BorderLayout.EAST);

        add(volverButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void mostrarMovimiento(int origen, int destino) {
        // Mostrar movimiento en el área de texto
        movimientosArea.append("Mover disco de torre " + (char) ('A' + origen) + " a torre " + (char) ('A' + destino) + "\n");
    }

    public void actualizarDiscos(List<JPanel> torres[], int origen, int destino) {
        // Actualiza los discos visualmente al moverlos entre torres
        SwingUtilities.invokeLater(() -> {
            JPanel torreOrigen = (JPanel) panelTorres.getComponent(origen);
            JPanel torreDestino = (JPanel) panelTorres.getComponent(destino);
            torreOrigen.revalidate();
            torreDestino.revalidate();
            torreOrigen.repaint();
            torreDestino.repaint();
        });
    }
}

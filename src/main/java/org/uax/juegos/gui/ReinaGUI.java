package org.uax.juegos.gui;

import org.uax.juegos.juegos.Reina;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ReinaGUI extends JFrame {
    private JPanel panelTablero;
    private JTextField reinasN;
    private JButton volver;
    private int[] solucion = new int[8]; // Por defecto 8x8

    public ReinaGUI() {
        setTitle("Juego de las reinas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // NORTH: Título, campo y botón
        JPanel panelSuperior = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Juego de las reinas", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        panelSuperior.add(titulo, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        reinasN = new JTextField("8", 5);
        JButton botonResolver = new JButton("Mostrar solución");
        inputPanel.add(new JLabel("Número de reinas:"));
        inputPanel.add(reinasN);
        inputPanel.add(botonResolver);

        panelSuperior.add(inputPanel, BorderLayout.SOUTH);
        add(panelSuperior, BorderLayout.NORTH);

        // CENTER: Panel del tablero
        panelTablero = new TableroPanel();
        add(panelTablero, BorderLayout.CENTER);

        // SOUTH: Botón volver
        volver = new JButton("Volver");
        add(volver, BorderLayout.SOUTH);
        volver.addActionListener((ActionEvent e) -> {
            SwingUtilities.invokeLater(() -> {
                new VentanaPrincipal().setVisible(true);
                this.setVisible(false);
            });
        });

        // Acción del botón
        botonResolver.addActionListener((ActionEvent e) -> {
            try {
                int n = Integer.parseInt(reinasN.getText());
                if (n < 4) {
                    JOptionPane.showMessageDialog(this, "Ingrese un número mayor o igual a 4.");
                    return;
                }

                Reina reina = new Reina(n);
                List<int[]> soluciones = reina.resolver();
                solucion = soluciones.isEmpty() ? new int[n] : soluciones.get(0);

                // Redibujar el tablero con nueva solución
                panelTablero.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Número no válido.");
            }
        });

        setSize(600, 600);
        setLocationRelativeTo(null);
    }

    // Clase del panel de dibujo
    private class TableroPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int n = solucion.length;
            int size = Math.min(getWidth(), getHeight()) / n;

            for (int fila = 0; fila < n; fila++) {
                for (int col = 0; col < n; col++) {
                    boolean blanco = (fila + col) % 2 == 0;
                    g.setColor(blanco ? Color.WHITE : Color.GRAY);
                    g.fillRect(col * size, fila * size, size, size);

                    if (solucion[fila] == col) {
                        g.setColor(Color.RED);
                        g.fillOval(col * size + size / 4, fila * size + size / 4, size / 2, size / 2);
                    }
                }
            }
        }
    }
}

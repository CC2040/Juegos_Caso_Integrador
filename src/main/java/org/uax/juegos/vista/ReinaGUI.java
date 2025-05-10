package org.uax.juegos.vista;

import javax.swing.*;
import java.awt.*;

// ReinaView.java
public class ReinaGUI extends JFrame {
    private JTextField input;
    private JButton resolver;
    private JButton siguiente;
    private JButton volver;
    private JButton guardar;
    private JButton historial;
    private JPanel tablero;
    private JTextArea historialArea;

    public ReinaGUI() {
        setTitle("N Reinas - MVC");
        setSize(600, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        input = new JTextField(5);
        resolver = new JButton("Resolver");
        siguiente = new JButton("Siguiente");
        volver = new JButton("Volver");
        guardar = new JButton("Guardar");
        historial = new JButton("Historial");
        siguiente.setEnabled(false);

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Número de reinas:"));
        topPanel.add(input);
        topPanel.add(resolver);
        topPanel.add(siguiente);
        topPanel.add(guardar);
        topPanel.add(historial);

        tablero = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        tablero.setPreferredSize(new Dimension(600, 600));

        historialArea = new JTextArea();
        historialArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(historialArea);
        scrollPane.setPreferredSize(new Dimension(200, 600));

        JPanel popPanel = new JPanel();
        popPanel.add(volver);

        add(topPanel, BorderLayout.NORTH);
        add(tablero, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.EAST);
        add(popPanel, BorderLayout.SOUTH);
    }

    public JTextField getInput() { return input; }
    public JButton getResolver() { return resolver; }
    public JButton getSiguiente() { return siguiente; }
    public JButton getGuardar() { return guardar; }
    public JButton getHistorial() { return historial; }
    public JTextArea getHistorialArea() { return historialArea; }
    public JButton getVolver() { return volver; }
    public JPanel getTablero() { return tablero; }
}

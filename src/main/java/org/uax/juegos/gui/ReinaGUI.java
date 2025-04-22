package org.uax.juegos.gui;

import javax.swing.*;
import java.awt.*;

// ReinaView.java
public class ReinaGUI extends JFrame {
    private JTextField input;
    private JButton resolver;
    private JButton siguiente;
    private JPanel tablero;

    public ReinaGUI() {
        setTitle("N Reinas - MVC");
        setSize(600, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        input = new JTextField(5);
        resolver = new JButton("Resolver");
        siguiente = new JButton("Siguiente");
        siguiente.setEnabled(false);

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Número de reinas:"));
        topPanel.add(input);
        topPanel.add(resolver);
        topPanel.add(siguiente);

        tablero = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        tablero.setPreferredSize(new Dimension(600, 600));

        add(topPanel, BorderLayout.NORTH);
        add(tablero, BorderLayout.CENTER);
    }

    public JTextField getInput() { return input; }
    public JButton getResolver() { return resolver; }
    public JButton getSiguiente() { return siguiente; }
    public JPanel getTablero() { return tablero; }
}

package org.uax.juegos.gui;

import org.uax.juegos.juegos.Hanoi;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HanoiGUI extends JFrame {
    private JPanel panelTorres;
    private JTextField discosInput;
    private JTextArea movimientosArea;
    private JButton iniciarButton;
    private List<JPanel>[] torres;
    private int numDiscos;

    public HanoiGUI() {
        setTitle("Torres de Hanoi");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        discosInput = new JTextField("3", 5);
        iniciarButton = new JButton("Iniciar");
        movimientosArea = new JTextArea();
        movimientosArea.setEditable(false);

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Número de discos:"));
        topPanel.add(discosInput);
        topPanel.add(iniciarButton);

        add(topPanel, BorderLayout.NORTH);

        panelTorres = new JPanel(new GridLayout(1, 3));
        torres = new List[3];

        for (int i = 0; i < 3; i++) {
            torres[i] = new ArrayList<>();
            JPanel torre = new JPanel();
            torre.setLayout(new BoxLayout(torre, BoxLayout.Y_AXIS));
            torre.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            torre.setBackground(Color.LIGHT_GRAY);
            torre.add(Box.createVerticalGlue());
            panelTorres.add(torre);
        }

        add(panelTorres, BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(movimientosArea);
        scroll.setPreferredSize(new Dimension(200, 0));
        add(scroll, BorderLayout.EAST);

        iniciarButton.addActionListener(e -> iniciarJuego());

        setVisible(true);
    }

    private void iniciarJuego() {
        try {
            numDiscos = Integer.parseInt(discosInput.getText());
            movimientosArea.setText("");

            for (int i = 0; i < 3; i++) torres[i].clear();
            panelTorres.removeAll();

            for (int i = 0; i < 3; i++) {
                JPanel torre = new JPanel();
                torre.setLayout(new BoxLayout(torre, BoxLayout.Y_AXIS));
                torre.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                torre.setBackground(Color.LIGHT_GRAY);
                torre.add(Box.createVerticalGlue());
                panelTorres.add(torre);
            }

            revalidate();
            repaint();

            for (int i = numDiscos; i >= 1; i--) {
                JPanel disco = crearDisco(i);
                torres[0].add(disco);
                JPanel torre = (JPanel) panelTorres.getComponent(0);
                torre.add(disco, torre.getComponentCount() - 1);
            }

            revalidate();
            repaint();

            Hanoi hanoi = new Hanoi(numDiscos);
            new Thread(() -> hanoi.resolver(numDiscos, 0, 2, 1, (from, to) -> {
                moverDisco(from, to);
                registrarMovimiento(from, to);
                pausar();
            })).start();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un número válido.");
        }
    }

    private JPanel crearDisco(int size) {
        JPanel disco = new JPanel();
        int height = 20;
        int width = 30 + size * 20;
        disco.setPreferredSize(new Dimension(width, height));
        disco.setMaximumSize(new Dimension(width, height));
        disco.setAlignmentX(Component.CENTER_ALIGNMENT);
        disco.setBackground(new Color((int)(Math.random() * 0x1000000)));
        disco.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return disco;
    }

    private void moverDisco(int origen, int destino) {
        if (torres[origen].isEmpty()) return;

        JPanel disco = torres[origen].remove(torres[origen].size() - 1);
        torres[destino].add(disco);

        SwingUtilities.invokeLater(() -> {
            JPanel torreOrigen = (JPanel) panelTorres.getComponent(origen);
            JPanel torreDestino = (JPanel) panelTorres.getComponent(destino);
            torreOrigen.remove(disco);
            torreDestino.add(disco, torreDestino.getComponentCount() - 1);
            torreOrigen.revalidate();
            torreDestino.revalidate();
            torreOrigen.repaint();
            torreDestino.repaint();
        });
    }

    private void registrarMovimiento(int origen, int destino) {
        SwingUtilities.invokeLater(() -> {
            movimientosArea.append("Mover disco de torre " + (origen + 1) + " a torre " + (destino + 1) + "\n");
        });
    }

    private void pausar() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}

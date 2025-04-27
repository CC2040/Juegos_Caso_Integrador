package org.uax.juegos.controlador.dominio;

import org.uax.juegos.vista.HanoiGUI;
import org.uax.juegos.vista.VentanaPrincipal;
import org.uax.juegos.modelo.Hanoi;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HanoiEvento {
    private HanoiGUI vista;
    private List<JPanel>[] torres;
    private int numDiscos;

    public HanoiEvento(HanoiGUI vista) {
        this.vista = vista;
        torres = new ArrayList[3];
        for (int i = 0; i < 3; i++) {
            torres[i] = new ArrayList<>();
        }

        vista.iniciarButton.addActionListener(e -> iniciarJuego());
        vista.volverButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                new VentanaPrincipal().setVisible(true);
                vista.dispose();
            });
        });
    }

    private void iniciarJuego() {
        try {
            numDiscos = Integer.parseInt(vista.discosInput.getText());
            if (numDiscos <= 0) throw new NumberFormatException();

            vista.movimientosArea.setText("");
            vista.panelTorres.removeAll();

            for (int i = 0; i < 3; i++) {
                torres[i].clear();
                JPanel torre = new JPanel();
                torre.setLayout(new BoxLayout(torre, BoxLayout.Y_AXIS));
                torre.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                torre.setBackground(Color.LIGHT_GRAY);
                torre.add(Box.createVerticalGlue());

                JLabel etiqueta = new JLabel(String.valueOf((char) ('A' + i)), JLabel.CENTER);
                etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);
                torre.add(etiqueta);

                vista.panelTorres.add(torre);
            }

            // Crear discos y agregarlos visualmente a la torre A
            for (int i = numDiscos; i >= 1; i--) {
                JPanel disco = crearDisco(i);
                torres[0].add(disco);
                JPanel torre = (JPanel) vista.panelTorres.getComponent(0);
                torre.add(disco, 1);
            }

            vista.revalidate();
            vista.repaint();

            Hanoi modelo = new Hanoi(numDiscos);
            for (int i = numDiscos; i >= 1; i--) {
                modelo.getTorres()[0].push(i);
            }

            new Thread(() -> modelo.resolver(numDiscos, 0, 2, 1, (from, to) -> {
                int discoMovido = obtenerNumeroDisco(from);
                moverDisco(from, to);
                registrarMovimiento(from, to, discoMovido);
                pausar();
            })).start();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Por favor, ingrese un número válido mayor que 0.");
        }
    }

    private JPanel crearDisco(int size) {
        JPanel disco = new JPanel();
        int height = 20;
        int width = 30 + size * 20;
        disco.setPreferredSize(new Dimension(width, height));
        disco.setMaximumSize(new Dimension(width, height));
        disco.setAlignmentX(Component.CENTER_ALIGNMENT);
        disco.setBackground(new Color((int) (Math.random() * 0xFFFFFF)));
        disco.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        disco.add(new JLabel(String.valueOf(size)));
        return disco;
    }

    private int obtenerNumeroDisco(int torre) {
        if (torres[torre].isEmpty()) return -1;
        JPanel panel = torres[torre].get(torres[torre].size() - 1);
        JLabel etiqueta = (JLabel) panel.getComponent(0);
        return Integer.parseInt(etiqueta.getText());
    }

    private void moverDisco(int origen, int destino) {
        if (torres[origen].isEmpty()) return;
        JPanel disco = torres[origen].remove(torres[origen].size() - 1);
        torres[destino].add(disco);

        SwingUtilities.invokeLater(() -> {
            JPanel torreOrigen = (JPanel) vista.panelTorres.getComponent(origen);
            JPanel torreDestino = (JPanel) vista.panelTorres.getComponent(destino);
            torreOrigen.remove(disco);
            torreDestino.add(disco, 1);
            torreOrigen.revalidate();
            torreDestino.revalidate();
            torreOrigen.repaint();
            torreDestino.repaint();
        });
    }

    private void registrarMovimiento(int origen, int destino, int disco) {
        vista.mostrarMovimiento(origen, destino, disco);
    }


    private void pausar() {
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

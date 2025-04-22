package org.uax.juegos.eventos;
import org.uax.juegos.juegos.Hanoi;
import org.uax.juegos.gui.HanoiGUI;
import org.uax.juegos.gui.VentanaPrincipal;
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
        torres = new List[3];
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
            vista.movimientosArea.setText("");
            vista.panelTorres.removeAll();

            for (int i = 0; i < 3; i++) {
                torres[i].clear();
                JPanel torre = new JPanel();
                torre.setLayout(new BoxLayout(torre, BoxLayout.Y_AXIS));
                torre.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                torre.setBackground(Color.LIGHT_GRAY);
                torre.add(Box.createVerticalGlue());
                vista.panelTorres.add(torre);

                // Etiqueta de torre (A, B, C)
                JLabel etiquetaTorre = new JLabel(String.valueOf((char) ('A' + i)), JLabel.CENTER);
                etiquetaTorre.setAlignmentX(Component.CENTER_ALIGNMENT);
                torre.add(etiquetaTorre);
            }

            for (int i = 1; i <= numDiscos; i++) {
                JPanel disco = crearDisco(i);
                torres[0].add(disco);  // Añadir el disco a la torre 0
                JPanel torre = (JPanel) vista.panelTorres.getComponent(0);
                torre.add(disco);  // Añadir el disco a la parte inferior (por defecto añade al final)
            }


            vista.revalidate();
            vista.repaint();

            Hanoi hanoi = new Hanoi(numDiscos);
            new Thread(() -> hanoi.resolver(numDiscos, 0, 2, 1, (from, to) -> {
                moverDisco(from, to);
                registrarMovimiento(from, to);
                pausar();
            })).start();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Por favor, ingrese un número válido.");
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
        disco.add(new JLabel(String.valueOf(size), JLabel.CENTER)); // El disco tiene un número
        return disco;
    }

    private void moverDisco(int origen, int destino) {
        if (torres[origen].isEmpty()) return;
        JPanel disco = torres[origen].remove(torres[origen].size() - 1);
        torres[destino].add(disco);

        SwingUtilities.invokeLater(() -> {
            JPanel torreOrigen = (JPanel) vista.panelTorres.getComponent(origen);
            JPanel torreDestino = (JPanel) vista.panelTorres.getComponent(destino);
            torreOrigen.remove(disco);
            torreDestino.add(disco, torreDestino.getComponentCount() - 1);
            torreOrigen.revalidate();
            torreDestino.revalidate();
            torreOrigen.repaint();
            torreDestino.repaint();
        });
    }

    private void registrarMovimiento(int origen, int destino) {
        vista.mostrarMovimiento(origen, destino);
    }

    private void pausar() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

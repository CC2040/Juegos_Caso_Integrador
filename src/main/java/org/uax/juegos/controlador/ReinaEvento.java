package org.uax.juegos.controlador;
import org.uax.juegos.modelo.Reina;
import org.uax.juegos.vista.ReinaGUI;
import org.uax.juegos.vista.VentanaPrincipal;
import java.awt.*;
import javax.swing.*;
import java.util.List;


// ReinaController.java
public class ReinaEvento {
    private Reina modelo;
    private ReinaGUI vista;
    private List<int[]> soluciones;
    private int indice = 0;

    public ReinaEvento(ReinaGUI vista) {
        this.vista = vista;
        initListeners();
    }

    private void initListeners() {
        vista.getResolver().addActionListener(e -> {
            int n = Integer.parseInt(vista.getInput().getText());
            modelo = new Reina(n);
            soluciones = modelo.resolver();
            if (soluciones.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "No hay solución");
            } else {
                indice = 0;
                dibujar(soluciones.get(indice));
                vista.getSiguiente().setEnabled(true);
            }
        });

        vista.getSiguiente().addActionListener(e -> {
            if (soluciones != null && !soluciones.isEmpty()) {
                indice = (indice + 1) % soluciones.size();
                dibujar(soluciones.get(indice));
            }
        });
        vista.getVolver().addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                new VentanaPrincipal().setVisible(true);
                vista.dispose();
            });
        });
    }

    private void dibujar(int[] solucion) {
        JPanel panel = vista.getTablero();
        Graphics g = panel.getGraphics();
        int size = panel.getWidth() / solucion.length;
        g.clearRect(0, 0, panel.getWidth(), panel.getHeight());

        for (int i = 0; i < solucion.length; i++) {
            for (int j = 0; j < solucion.length; j++) {
                g.setColor((i + j) % 2 == 0 ? Color.WHITE : Color.GRAY);
                g.fillRect(j * size, i * size, size, size);
            }
        }

        g.setColor(Color.RED);
        for (int i = 0; i < solucion.length; i++) {
            g.fillOval(solucion[i] * size + size / 4, i * size + size / 4, size / 2, size / 2);
        }
    }
}


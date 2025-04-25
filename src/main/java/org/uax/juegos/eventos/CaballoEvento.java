package org.uax.juegos.eventos;

import org.uax.juegos.gui.CaballoGUI;
import org.uax.juegos.gui.VentanaPrincipal;
import org.uax.juegos.juegos.Caballo;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CaballoEvento {
    private CaballoGUI vista;
    private Caballo caballo;

    public CaballoEvento(CaballoGUI vista) {
        this.vista = vista;

        vista.getIniciar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarJuego();
            }
        });

        vista.getVolver().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vista.dispose();
                new VentanaPrincipal().setVisible(true);
            }
        });
    }


    private void iniciarJuego() {
        try {
            int filasN = Integer.parseInt(vista.filas.getText());
            int columnasN = Integer.parseInt(vista.columnas.getText());
            int x = Integer.parseInt(vista.posicionX.getText());
            int y = Integer.parseInt(vista.posicionY.getText());

            ArrayList<int[]> posicionesIniciales = new ArrayList<>();
            posicionesIniciales.add(new int[]{x, y});
            caballo = new Caballo(0, "Caballo", posicionesIniciales);

            vista.movimientosArea.setText("Calculando solución...\n");

            SwingWorker<Void, int[]> worker = new SwingWorker<Void,int[]>() {
                ArrayList<int[]> recorrido;

                @Override
                protected Void doInBackground() {
                    recorrido = caballo.resolverCaballo(filasN, columnasN);
                    return null;
                }

                @Override
                protected void done() {
                    if (recorrido.isEmpty()) {
                        vista.movimientosArea.setText("No se encontró solución.");
                        return;
                    }

                    StringBuilder sb = new StringBuilder("Recorrido:\n");
                    ArrayList<int[]> visitadas = new ArrayList<>();
                    Timer timer = new Timer(400, null);
                    final int[] paso = {1}; // ya se agregó el primer paso antes

                    mostrarTablero(filasN, columnasN, recorrido, paso[0]);

                    timer.addActionListener(e -> {
                        if (paso[0] >= recorrido.size()) {
                            ((Timer) e.getSource()).stop();
                            vista.movimientosArea.append("\nTotal de movimientos: " + recorrido.size());
                            return;
                        }

                        mostrarTablero(filasN, columnasN, recorrido, paso[0] + 1);
                        int[] pos = recorrido.get(paso[0]);
                        sb.append("Paso " + (paso[0] + 1) + ": (" + pos[0] + ", " + pos[1] + ")\n");
                        vista.movimientosArea.setText(sb.toString());
                        paso[0]++;
                    });


                    timer.start();
                }
            };

            worker.execute();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Por favor ingrese números válidos.");
        }
    }

    private void mostrarTablero(int filas, int columnas, ArrayList<int[]> recorrido, int pasoActual) {
        JPanel tablero = vista.tablero;
        tablero.removeAll();
        tablero.setLayout(new GridLayout(filas, columnas));

        int[][] tableroNumeros = new int[filas][columnas];
        for (int i = 0; i < pasoActual; i++) {
            int[] paso = recorrido.get(i);
            tableroNumeros[paso[0]][paso[1]] = i + 1; // Guardamos el número del paso (1-based)
        }

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                JPanel celda = new JPanel(new BorderLayout());
                celda.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                if (tableroNumeros[i][j] > 0) {
                    JLabel label = new JLabel(String.valueOf(tableroNumeros[i][j]), SwingConstants.CENTER);
                    celda.add(label, BorderLayout.CENTER);

                    if (i == recorrido.get(pasoActual - 1)[0] && j == recorrido.get(pasoActual - 1)[1]) {
                        celda.setBackground(Color.RED); // posición actual
                    } else {
                        celda.setBackground(Color.GREEN); // recorrido anterior
                    }
                } else {
                    celda.setBackground(Color.WHITE); // no visitado
                }

                tablero.add(celda);
            }
        }

        tablero.revalidate();
        tablero.repaint();
    }


    private boolean contieneMovimiento(ArrayList<int[]> movimientos, int x, int y) {
        for (int[] m : movimientos) {
            if (m[0] == x && m[1] == y) return true;
        }
        return false;
    }
}

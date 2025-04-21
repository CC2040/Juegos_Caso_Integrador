package org.uax.juegos.gui;

import org.uax.juegos.juegos.Caballo;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CaballoGUI extends JFrame {
    private JTextField filas;
    private JTextField columnas;
    private JTextField posicionX;
    private JTextField posicionY;
    private JButton iniciar;
    private JButton volver;
    private JPanel tablero;
    private JTextArea movimientosArea;
    private Caballo caballo;

    public CaballoGUI() {
        setTitle("Caballo");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        filas = new JTextField();
        columnas = new JTextField();
        posicionX = new JTextField();
        posicionY = new JTextField();
        iniciar = new JButton("Iniciar");

        iniciar.setPreferredSize(new Dimension(80, 30));
        filas.setPreferredSize(new Dimension(80, 30));
        posicionX.setPreferredSize(new Dimension(80, 30));
        posicionY.setPreferredSize(new Dimension(80, 30));
        columnas.setPreferredSize(new Dimension(80, 30));

        //Elementos NORTH
        JLabel titulo = new JLabel("Juego del Caballo");
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel instrucciones = new JLabel("Ingrese el número de filas, columnas del tablero (fila,columna) y la posicion inicial(x,y):");
        instrucciones.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel datosPanel = new JPanel(new GridLayout(4, 1, 10, 5));
        datosPanel.add(titulo);
        datosPanel.add(instrucciones);

        JPanel datosPanel2 = new JPanel(new GridLayout(2, 3, 10, 10));
        datosPanel2.add(columnas);
        datosPanel2.add(filas);
        datosPanel2.add(iniciar);
        datosPanel2.add(posicionX);
        datosPanel2.add(posicionY);
        datosPanel.add(datosPanel2);

        add(datosPanel, BorderLayout.NORTH);

        // Elementos CENTER
        tablero = new JPanel();
        tablero.setBackground(Color.WHITE);
        add(tablero, BorderLayout.CENTER);

        //Elementos EAST
        movimientosArea = new JTextArea();
        movimientosArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(movimientosArea);
        scrollPane.setPreferredSize(new Dimension(200, 0));
        add(scrollPane, BorderLayout.EAST);

        // Elementos SOUTH
        volver = new JButton("Volver");
        volver.setPreferredSize(new Dimension(80, 30));
        add(volver, BorderLayout.SOUTH);

        volver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new VentanaPrincipal().setVisible(true);
            }
        });
        iniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarJuego();
            }
        });

        setVisible(true);
    }

    private void iniciarJuego() {
        try {
            int filasN = Integer.parseInt(filas.getText());
            int columnasN = Integer.parseInt(columnas.getText());
            int x = Integer.parseInt(posicionX.getText());
            int y = Integer.parseInt(posicionY.getText());

            ArrayList<int[]> posicionesIniciales = new ArrayList<>();
            posicionesIniciales.add(new int[]{x, y});
            caballo = new Caballo(0, "Caballo", posicionesIniciales);

            movimientosArea.setText("Calculando solución...\n");

            // Ejecutar en segundo plano
            SwingWorker<Void, int[]> worker = new SwingWorker<Void,int []>() {
                ArrayList<int[]> recorrido;

                @Override
                protected Void doInBackground() throws Exception {
                    recorrido = caballo.resolverCaballo(filasN, columnasN);
                    return null;
                }

                @Override
                protected void done() {
                    if (recorrido.isEmpty()) {
                        movimientosArea.setText("No se encontró solución.");
                        return;
                    }

                    StringBuilder sb = new StringBuilder("Recorrido:\n");
                    ArrayList<int[]> visitadas = new ArrayList<>();

                    Timer timer = new Timer(400, null);
                    final int[] paso = {0};

                    timer.addActionListener(e -> {
                        if (paso[0] >= recorrido.size()) {
                            ((Timer) e.getSource()).stop();
                            movimientosArea.append("\nTotal de movimientos: " + recorrido.size());
                            return;
                        }

                        int[] pos = recorrido.get(paso[0]);
                        visitadas.add(pos);
                        mostrarTablero(filasN, columnasN, pos, visitadas);
                        sb.append("Paso " + (paso[0] + 1) + ": (" + pos[0] + ", " + pos[1] + ")\n");
                        movimientosArea.setText(sb.toString());
                        paso[0]++;
                    });

                    timer.start();
                }
            };

            worker.execute();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese números válidos.");
        }
    }



    private void mostrarTablero(int filas, int columnas, int[] actual, ArrayList<int[]> visitadas) {
        tablero.removeAll();
        tablero.setLayout(new GridLayout(filas, columnas));

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                JPanel celda = new JPanel();
                celda.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                if (i == actual[0] && j == actual[1]) {
                    celda.setBackground(Color.RED); // actual en rojo
                } else if (contieneMovimiento(visitadas, i, j)) {
                    celda.setBackground(Color.GREEN); // visitadas en verde
                } else {
                    celda.setBackground(Color.WHITE);
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

    public JTextField getFilasField() {
        return filas;
    }

    public JTextField getColumnasField() {
        return columnas;
    }

    public JButton getIniciarButton() {
        return iniciar;
    }

    public JPanel getTableroPanel() {
        return tablero;
    }

    public JTextArea getMovimientosArea() {
        return movimientosArea;
    }

}
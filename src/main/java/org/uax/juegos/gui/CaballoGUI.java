package org.uax.juegos.gui;

import org.uax.juegos.eventos.CaballoEvento;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CaballoGUI extends JFrame {
    private JTextField tamanoTablero;
    private JButton generarTablero;
    private JButton iniciar;
    private JButton volver;
    private JPanel tablero;
    private JButton detener;
    private JButton continuar;
    private JTextArea movimientosArea;
    private int[][] tableroVisual;
    private boolean tableroGenerado = false; // Variable para verificar si el tablero ha sido generado
    private int xSeleccionado = -1, ySeleccionado = -1; // Variables para la posición seleccionada

    public CaballoGUI() {
        setTitle("Juego del Caballo");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel superior (NORTH) - Entrada de datos
        JPanel entradaPanel = new JPanel(new GridLayout(3, 1, 10, 5));
        entradaPanel.add(new JLabel("Tamaño del tablero (n x n):", SwingConstants.LEFT));
        tamanoTablero = new JTextField();
        entradaPanel.add(tamanoTablero);

        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        generarTablero = new JButton("Generar Tablero");
        iniciar = new JButton("Iniciar");
        detener = new JButton("Detener");
        continuar = new JButton("Continuar");
        botonesPanel.add(generarTablero);
        botonesPanel.add(iniciar);
        botonesPanel.add(detener);
        botonesPanel.add(continuar);
        iniciar.setEnabled(false);
        detener.setEnabled(false);
        entradaPanel.add(botonesPanel);
        add(entradaPanel, BorderLayout.NORTH);

        // Panel central (CENTER) - Tablero
        tablero = new JPanel();
        tablero.setBackground(Color.WHITE);
        add(tablero, BorderLayout.CENTER);

        // Panel lateral (EAST) - Área de movimientos
        movimientosArea = new JTextArea();
        movimientosArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(movimientosArea);
        scrollPane.setPreferredSize(new Dimension(200, 0));
        add(scrollPane, BorderLayout.EAST);

        // Panel inferior (SOUTH) - Botón volver
        volver = new JButton("Volver");
        add(volver, BorderLayout.SOUTH);


        // Acción para seleccionar posición en el tablero
        tablero.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!tableroGenerado) {
                    JOptionPane.showMessageDialog(CaballoGUI.this, "Primero genere el tablero.");
                    return;
                }

                // Asegurarse de que tamanoTablero no esté vacío o tenga un valor válido
                int tamanoTableroValue;
                try {
                    tamanoTableroValue = Integer.parseInt(tamanoTablero.getText().trim());
                    if (tamanoTableroValue <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(CaballoGUI.this, "Ingrese un número válido para el tamaño del tablero.");
                    return;
                }

                // Asegurarse de que solo se pinte la nueva celda seleccionada
                if (xSeleccionado != -1 && ySeleccionado != -1) {
                    // Limpiar la celda previamente seleccionada
                    Component[] celdas = tablero.getComponents();
                    if (xSeleccionado >= 0 && ySeleccionado >= 0) {
                        JPanel panelAnterior = (JPanel) celdas[xSeleccionado * tamanoTableroValue + ySeleccionado];
                        panelAnterior.setBackground(Color.WHITE); // Restaurar color de la celda anterior
                    }
                }

                int fila = e.getY() / (tablero.getHeight() / tamanoTableroValue);
                int col = e.getX() / (tablero.getWidth() / tamanoTableroValue);

                // Colorear la celda seleccionada
                Component[] celdas = tablero.getComponents();
                if (fila >= 0 && col >= 0 && fila < tamanoTableroValue && col < tamanoTableroValue) {
                    JPanel panel = (JPanel) celdas[fila * tamanoTableroValue + col];
                    panel.setBackground(Color.GREEN); // Colorear la celda seleccionada
                    xSeleccionado = fila;
                    ySeleccionado = col;
                }
            }
        });

    }

    // Getters y Setters
    public JTextField getTamanoTablero() {
        return tamanoTablero;
    }

    public JButton getGenerarTablero() {
        return generarTablero;
    }

    public JButton getIniciar() {
        return iniciar;
    }

    public JButton getVolver() {
        return volver;
    }

    public JPanel getTablero() {
        return tablero;
    }

    public JTextArea getMovimientosArea() {
        return movimientosArea;
    }

    public boolean isTableroGenerado() {
        return tableroGenerado;
    }

    public void setTableroGenerado(boolean tableroGenerado) {
        this.tableroGenerado = tableroGenerado;
    }

    public int getXSeleccionado() {
        return xSeleccionado;
    }

    public int getYSeleccionado() {
        return ySeleccionado;
    }
    public JButton getDetener() {
        return detener;
    }

    public JButton getContinuar() {
        return continuar;
    }

}

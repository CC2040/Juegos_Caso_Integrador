package org.uax.juegos.gui;
import org.uax.juegos.eventos.CaballoEvento;
import javax.swing.*;
import java.awt.*;

public class CaballoGUI extends JFrame {
    public JTextField filas;
    public JTextField columnas;
    public JTextField posicionX;
    public JTextField posicionY;
    public JButton iniciar;
    public JButton volver;
    public JPanel tablero;
    public JTextArea movimientosArea;

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
        volver = new JButton("Volver");

        iniciar.setPreferredSize(new Dimension(80, 30));
        volver.setPreferredSize(new Dimension(80, 30));

        JPanel datosPanel = new JPanel(new GridLayout(4, 1, 10, 5));
        datosPanel.add(new JLabel("Juego del Caballo", SwingConstants.CENTER));
        datosPanel.add(new JLabel("Ingrese filas, columnas y posición inicial (x, y):", SwingConstants.LEFT));

        JPanel datosPanel2 = new JPanel(new GridLayout(2, 3, 10, 10));
        datosPanel2.add(columnas);
        datosPanel2.add(filas);
        datosPanel2.add(iniciar);
        datosPanel2.add(posicionX);
        datosPanel2.add(posicionY);
        datosPanel.add(datosPanel2);
        add(datosPanel, BorderLayout.NORTH);

        tablero = new JPanel();
        tablero.setBackground(Color.WHITE);
        add(tablero, BorderLayout.CENTER);

        movimientosArea = new JTextArea();
        movimientosArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(movimientosArea);
        scrollPane.setPreferredSize(new Dimension(200, 0));
        add(scrollPane, BorderLayout.EAST);

        add(volver, BorderLayout.SOUTH);
    }

    // Getters y Setters botones
    public JTextField getFilas() {
        return filas;
    }

    public void setFilas(JTextField filas) {
        this.filas = filas;
    }

    public JTextField getColumnas() {
        return columnas;
    }

    public void setColumnas(JTextField columnas) {
        this.columnas = columnas;
    }

    public JTextField getPosicionX() {
        return posicionX;
    }

    public void setPosicionX(JTextField posicionX) {
        this.posicionX = posicionX;
    }

    public JTextField getPosicionY() {
        return posicionY;
    }

    public void setPosicionY(JTextField posicionY) {
        this.posicionY = posicionY;
    }

    public JButton getIniciar() {
        return iniciar;
    }

    public void setIniciar(JButton iniciar) {
        this.iniciar = iniciar;
    }

    public JButton getVolver() {
        return volver;
    }

    public void setVolver(JButton volver) {
        this.volver = volver;
    }

    public JPanel getTablero() {
        return tablero;
    }

    public void setTablero(JPanel tablero) {
        this.tablero = tablero;
    }

    public JTextArea getMovimientosArea() {
        return movimientosArea;
    }

    public void setMovimientosArea(JTextArea movimientosArea) {
        this.movimientosArea = movimientosArea;
    }

}

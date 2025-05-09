package org.uax.juegos.vista;
import org.uax.juegos.controlador.CaballoEvento;
import org.uax.juegos.controlador.ReinaEvento;
import org.uax.juegos.controlador.HanoiEvento;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

public class VentanaPrincipal extends JFrame {
    public VentanaPrincipal() {
        // Configurar la ventana principal
        setTitle("Juegos");
        setSize(500, 400); // Tamaño fijo de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // Hacer la ventana no redimensionable
        setLocationRelativeTo(null); // Centrar en la pantalla

        // Crear componentes
        JLabel titulo = new JLabel("Seleccione el tipo de juego que desea resolver");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        JButton btnCaballo = new JButton("Caballo");
        JButton btnReinas = new JButton("Reinas");
        JButton btnHanoi = new JButton("Hanoi");
        JButton btnSalir = new JButton("Salir");

        // Configurar el layout
        setLayout(new BorderLayout(10, 20));

        // Panel para los botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(4, 1, 10, 10));
        panelBotones.add(btnCaballo);
        panelBotones.add(btnReinas);
        panelBotones.add(btnHanoi);
        panelBotones.add(btnSalir);

        // Añadir componentes a la ventana
        add(titulo, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);

        // Añadir márgenes
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50));

        // Añadir acción a los botones (ejemplo básico)
        btnCaballo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        CaballoGUI vista = new CaballoGUI();
                        new CaballoEvento(vista);
                        vista.setVisible(true);
                        VentanaPrincipal.this.dispose();
                    }
                });
            }
        });

        btnHanoi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    HanoiGUI gui = new HanoiGUI();
                    new HanoiEvento(gui);
                    VentanaPrincipal.this.dispose();
                });
            }
        });

        btnReinas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    ReinaGUI vista = new ReinaGUI();
                    new ReinaEvento(vista);
                    vista.setVisible(true);
                    VentanaPrincipal.this.dispose();
                });
            }
        });
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}

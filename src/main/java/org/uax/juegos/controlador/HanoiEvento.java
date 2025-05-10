package org.uax.juegos.controlador;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.uax.juegos.vista.HanoiGUI;
import org.uax.juegos.vista.VentanaPrincipal;
import org.uax.juegos.modelo.dominio.Hanoi;
import org.uax.juegos.modelo.persistencia.HanoiPersistencia.HanoiMovimiento;
import org.uax.juegos.modelo.persistencia.HanoiPersistencia.HanoiPartida;
import org.uax.juegos.util.HibernateUtil;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HanoiEvento {
    private HanoiGUI vista;
    private List<JPanel>[] torres;
    private int numDiscos;
    // Variables para persistir datos
    private HanoiPartida partida;
    private List<HanoiMovimiento> movimientosPersistencia;
    private String historialMensajeGuardado;

    public HanoiEvento(HanoiGUI vista) {
        this.vista = vista;
        torres = new ArrayList[3];
        for (int i = 0; i < 3; i++) {
            torres[i] = new ArrayList<>();
        }
        movimientosPersistencia = new ArrayList<>();
        vista.iniciarButton.addActionListener(e -> iniciarJuego());
        vista.volverButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                new VentanaPrincipal().setVisible(true);
                vista.dispose();
            });
        });
        vista.guardarButton.addActionListener(e -> guardarMovimientos());
        vista.mostrarHistorialButton.addActionListener(e -> mostrarHistorial());
    }

    private void iniciarJuego() {
        try {
            numDiscos = Integer.parseInt(vista.discosInput.getText());
            if (numDiscos <= 0) throw new NumberFormatException();

            vista.movimientosArea.setText("");
            vista.panelTorres.removeAll();
            // Inicializar la partida y reiniciar la lista de movimientos
            partida = new HanoiPartida();
            movimientosPersistencia.clear();

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

            // Inicializar la partida y reiniciar la lista de movimientos
            partida = new HanoiPartida();
            movimientosPersistencia.clear();

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
        String detalle = "Mover disco " + disco + " de torre " + (char) ('A' + origen) + " a torre " +
                (char) ('A' + destino);
        vista.mostrarMovimiento(origen, destino, disco);
        // El campo 'movimiento' se asigna aquí con el número del disco movido (puede ser cualquier otro indicador)
        HanoiMovimiento hm = new HanoiMovimiento(disco, detalle, partida);
        movimientosPersistencia.add(hm);
    }


    private void pausar() {
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void guardarMovimientos() {
        if (partida != null && !movimientosPersistencia.isEmpty()) {
            new Thread(() -> {
                try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                    Transaction tx = session.beginTransaction();
                    try {
                        partida.setMovimientos(movimientosPersistencia);
                        session.save(partida);
                        tx.commit();
                    } catch (Exception e) {
                        if (tx != null) {
                            tx.rollback();
                        }
                        e.printStackTrace();
                    }
                }
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String fechaString = sdf.format(partida.getFecha());
                historialMensajeGuardado = "Partida#" + partida.getId() + " (" + fechaString + "):\n"
                        + vista.movimientosArea.getText() + "\n";
                SwingUtilities.invokeLater(() -> {
                    // Se muestra un mensaje de confirmación sin actualizar el área de historial.
                    JOptionPane.showMessageDialog(vista,
                            "Partida guardada.",
                            "Guardado", JOptionPane.INFORMATION_MESSAGE);
                });
            }).start();
        } else {
            JOptionPane.showMessageDialog(vista, "No hay movimientos para guardar.");
        }
    }

    private void mostrarHistorial() {
        new Thread(() -> {
            List<HanoiPartida> partidas;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                partidas = session.createQuery(
                        "select distinct p from HanoiPartida p left join fetch p.movimientos",
                        HanoiPartida.class
                ).list();
            }
            StringBuilder mensaje = new StringBuilder();
            for (HanoiPartida p : partidas) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String fechaString = sdf.format(p.getFecha());
                mensaje.append("Partida#").append(p.getId())
                        .append(" (").append(fechaString).append("):\n");
                if (p.getMovimientos() != null && !p.getMovimientos().isEmpty()) {
                    for (HanoiMovimiento hm : p.getMovimientos()) {
                        mensaje.append(hm.getDetalle()).append("\n");
                    }
                } else {
                    mensaje.append("Sin movimientos.\n");
                }
                mensaje.append("\n");
            }
            String historial = mensaje.toString();
            SwingUtilities.invokeLater(() -> {
                if (!historial.isEmpty()) {
                    vista.historialArea.setText(historial);
                } else {
                    JOptionPane.showMessageDialog(vista,"No hay historial disponible.");
                }
            });
        }).start();
    }
}

package org.uax.juegos.controlador;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.uax.juegos.modelo.persistencia.ReinaPersistencia.ReinaMovimiento;
import org.uax.juegos.modelo.persistencia.ReinaPersistencia.ReinaPartida;
import org.uax.juegos.util.HibernateUtil;

import org.uax.juegos.modelo.dominio.Reina;
import org.uax.juegos.vista.ReinaGUI;
import org.uax.juegos.vista.VentanaPrincipal;
import java.awt.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;

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
            String inputText = vista.getInput().getText().trim();
            if (inputText.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Rellene los campos");
                return;
            }
            int n;
            try {
                n = Integer.parseInt(inputText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "Introduce un número válido");
                return;
            }
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

        vista.getGuardar().addActionListener(e -> {
            if (soluciones != null && !soluciones.isEmpty()) {
                guardarPartidaEnBD(soluciones);
            } else {
                JOptionPane.showMessageDialog(vista, "No hay soluciones para guardar.");
            }
        });
    
        vista.getHistorial().addActionListener(e -> {
            mostrarHistorial();
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

    private void guardarPartidaEnBD(List<int[]> soluciones) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            // Crear una nueva partida
            ReinaPartida partida = new ReinaPartida();
            List<ReinaMovimiento> movimientosPersistencia = new ArrayList<>();

            // Convertir cada solución en una entidad persistente
            for (int[] solucion : soluciones) {
                String solucionTexto = convertirSolucionAString(solucion);
                ReinaMovimiento movimiento = new ReinaMovimiento(solucionTexto, partida);
                movimientosPersistencia.add(movimiento);
            }

            partida.setMovimientos(movimientosPersistencia);

            // Guardar la partida y los movimientos
            session.persist(partida);
            tx.commit();

            JOptionPane.showMessageDialog(vista, "¡Partida guardada en la base de datos!");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            JOptionPane.showMessageDialog(vista, "Error al guardar: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    private String convertirSolucionAString(int[] solucion) {
        StringBuilder sb = new StringBuilder();
        for (int pos : solucion) {
            sb.append(pos).append(",");
        }
        return sb.deleteCharAt(sb.length() - 1).toString(); // Eliminar la última coma
    }

    private void mostrarHistorial() {
        vista.getHistorialArea().setText("Cargando historial...\n");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<ReinaPartida> partidas = session.createQuery("from ReinaPartida", ReinaPartida.class).list();
            if (partidas.isEmpty()) {
                vista.getHistorialArea().setText("No hay partidas guardadas.\n");
                return;
            }
    
            StringBuilder historial = new StringBuilder();
            for (ReinaPartida partida : partidas) {
                historial.append("Partida ID: ").append(partida.getId())
                         .append(", Fecha: ").append(partida.getFecha()).append("\n");
    
                for (ReinaMovimiento movimiento : partida.getMovimientos()) {
                    historial.append("  - Solución: ").append(convertirSolucionACoordenadas(movimiento.getSolucion())).append("\n");
                }
            }
            vista.getHistorialArea().setText(historial.toString());
        } catch (Exception e) {
            vista.getHistorialArea().setText("Error al cargar el historial: " + e.getMessage());
        }
    }

    private String convertirSolucionACoordenadas(String solucionTexto) {
        String[] columnas = solucionTexto.split(","); // Dividir la solución en columnas
        StringBuilder coordenadas = new StringBuilder();
    
        for (int fila = 0; fila < columnas.length; fila++) {
            coordenadas.append("(").append(fila).append(", ").append(columnas[fila]).append(")");
            if (fila < columnas.length - 1) {
                coordenadas.append(", "); // Agregar coma entre coordenadas
            }
        }
    
        return coordenadas.toString();
    }
}
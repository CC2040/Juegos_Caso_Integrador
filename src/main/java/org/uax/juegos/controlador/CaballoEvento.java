    package org.uax.juegos.controlador;

    import org.uax.juegos.vista.CaballoGUI;
    import org.uax.juegos.vista.VentanaPrincipal;
    import org.uax.juegos.modelo.dominio.Caballo;
import org.uax.juegos.modelo.persistencia.CaballoPersistencia.CaballoMovimiento;
import org.uax.juegos.modelo.persistencia.CaballoPersistencia.CaballoPartida;
import org.hibernate.Session;
    import org.hibernate.Transaction;
    import org.uax.juegos.util.HibernateUtil;
    import javax.swing.JOptionPane;
    import java.util.ArrayList;
    import javax.swing.*;
    import java.awt.*;
    import java.util.List;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;

    public class CaballoEvento implements ActionListener {
        private final CaballoGUI vista;
        private int tamano;
        private int[][] tableroVisual;
        private Timer timer;
        private boolean simulacionEnCurso = false;  // Para saber si la simulación está en curso
        private int pasoActual = 0;  // Mantener la referencia del paso actual en el recorrido
        private ArrayList<int[]> recorridoActual; // Para guardar el recorrido actual


        public CaballoEvento(CaballoGUI vista) {
            this.vista = vista;

            // Enlazar botones con eventos
            vista.getGenerarTablero().addActionListener(this);
            vista.getIniciar().addActionListener(this);
            vista.getVolver().addActionListener(this);
            vista.getDetener().addActionListener(this);
            vista.getContinuar().addActionListener(this);
            vista.getReiniciar().addActionListener(this);
            vista.getGuardar().addActionListener(this);
            vista.getHistorial().addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object fuente = e.getSource();

            if (fuente == vista.getGenerarTablero()) {
                generarTablero();
            }

            if (fuente == vista.getIniciar()) {
                iniciarRecorrido();
            }
            if (fuente == vista.getVolver()) {
                SwingUtilities.invokeLater(() -> {
                    new VentanaPrincipal().setVisible(true);
                    vista.dispose();
                });
            }
            if (fuente == vista.getDetener()) {
                detenerSimulacion();
            }
            if (fuente == vista.getContinuar()) {
                continuarSimulacion();
            }
            if (fuente == vista.getReiniciar()) {
                reiniciarTablero();

            }
            if (fuente == vista.getGuardar()) {
                if (recorridoActual != null && !recorridoActual.isEmpty()) {
                    guardarPartidaEnBD(recorridoActual);
                } else {
                    JOptionPane.showMessageDialog(vista, "No hay un recorrido para guardar.");
                }
            }
            if (fuente == vista.getHistorial()) {
                mostrarDatosEnGUI();
            }
        }

        private void generarTablero() {
            try {
                tamano = Integer.parseInt(vista.getTamanoTablero().getText().trim());
                if (tamano <= 0) throw new NumberFormatException();

                vista.getTablero().removeAll();
                vista.getTablero().setLayout(new GridLayout(tamano, tamano));
                tableroVisual = new int[tamano][tamano];

                for (int i = 0; i < tamano; i++) {
                    for (int j = 0; j < tamano; j++) {
                        JPanel celda = new JPanel();
                        celda.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        celda.setBackground(Color.WHITE);
                        vista.getTablero().add(celda);
                    }
                }

                vista.getTablero().revalidate();
                vista.getTablero().repaint();
                vista.setTableroGenerado(true);  // El tablero ha sido generado
                vista.getIniciar().setEnabled(true);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "Ingrese un número válido para el tamaño del tablero.");
            }
            vista.getDetener().setEnabled(false);
            vista.getContinuar().setEnabled(false);
        }

        private void iniciarRecorrido() {
            if (!vista.isTableroGenerado()) {
                JOptionPane.showMessageDialog(vista, "Primero debe generar el tablero.");
                return;
            }
                try {
                    int x = vista.getXSeleccionado();
                    int y = vista.getYSeleccionado();

                    if (x == -1 || y == -1) {
                        JOptionPane.showMessageDialog(vista, "Seleccione una posición inicial en el tablero.");
                        return;
                    }

                    ArrayList<int[]> inicio = new ArrayList<>();
                    inicio.add(new int[]{x, y});

                    Caballo caballo = new Caballo(0, "Caballo", inicio);
                    this.recorridoActual = caballo.resolverCaballo(tamano, tamano);
                    //guardarPartidaEnBD(recorridoActual);

                    if (recorridoActual.isEmpty()) {
                        JOptionPane.showMessageDialog(vista, "No se pudo encontrar una solución.");
                        return;
                    }

                    mostrarRecorridoPasoAPaso(recorridoActual);
                    vista.getIniciar().setEnabled(false);
                    vista.getDetener().setEnabled(true);
                    vista.habilitarSeleccion(false);
                    vista.getContinuar().setEnabled(false);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(vista, "Error en el inicio del recorrido.");
                }

        }

        private void mostrarRecorridoPasoAPaso(ArrayList<int[]> recorrido) {
            vista.getMovimientosArea().setText("");  // Limpiar el área de texto de movimientos

            // Limpiar tablero visual
            Component[] celdas = vista.getTablero().getComponents();
            for (Component celda : celdas) {
                if (celda instanceof JPanel) {
                    JPanel panel = (JPanel) celda;
                    panel.setBackground(Color.WHITE); // Poner todo el tablero en blanco
                    panel.removeAll();
                }
            }

            // Crear un Timer que actualizará el tablero paso a paso
            timer = new Timer(200, null);

            // Agregar un listener al Timer para que actualice el tablero cada 200 ms
            timer.addActionListener(e -> {
                if (pasoActual >= recorrido.size()) {
                    timer.stop();  // Detener el Timer cuando se termine el recorrido
                    vista.getMovimientosArea().append("\nTotal de movimientos: " + recorrido.size());
                    vista.getDetener().setEnabled(false);  // Deshabilitar el botón de Detener
                    vista.getContinuar().setEnabled(false);  // Deshabilitar el botón de Continuar
                    vista.getReiniciar().setEnabled(true);
                    return;
                }

                // Actualizar el tablero visual
                int[] pos = recorrido.get(pasoActual);
                int index = pos[0] * tamano + pos[1];

                if (index < celdas.length && celdas[index] instanceof JPanel) {
                    JPanel panel = (JPanel) celdas[index];
                    panel.setBackground(Color.CYAN);  // Resaltar la celda
                    JLabel num = new JLabel(String.valueOf(pasoActual + 1));
                    panel.add(num);  // Mostrar el número del paso en la celda
                }

                // Mostrar el paso actual en el área de texto
                vista.getMovimientosArea().append("Paso " + (pasoActual + 1) + ": (" + pos[0] + "," + pos[1] + ")\n");

                pasoActual++;
                vista.getTablero().repaint();  // Redibujar el tablero
            });

            // Habilitar los botones de Detener y Continuar
            vista.getDetener().setEnabled(true);
            vista.getContinuar().setEnabled(true);

            // Iniciar el Timer (solo una vez)
            simulacionEnCurso = true;
            timer.start();
        }
        private void detenerSimulacion() {
            if (simulacionEnCurso) {
                timer.stop();  // Detener el Timer
                simulacionEnCurso = false;
                vista.getDetener().setEnabled(false);  // Deshabilitar el botón de Detener
                vista.getContinuar().setEnabled(true);  // Habilitar el botón de Continuar
            }
        }

        private void continuarSimulacion() {
            if (!simulacionEnCurso) {
                // Continuar el recorrido desde el paso actual
                simulacionEnCurso = true;
                timer.start();
                vista.getDetener().setEnabled(true);  // Habilitar el botón de Detener
                vista.getContinuar().setEnabled(false);  // Deshabilitar el botón de Continuar
            }
        }
        private void reiniciarTablero() {
            if (!vista.isTableroGenerado()) {
                JOptionPane.showMessageDialog(vista, "Primero genere el tablero.");
                return;
            }

            // Limpiar el tablero
            Component[] celdas = vista.getTablero().getComponents();
            for (Component celda : celdas) {
                if (celda instanceof JPanel) {
                    JPanel panel = (JPanel) celda;
                    panel.setBackground(Color.WHITE);
                    panel.removeAll();
                }
            }

            // Resetear selección
            vista.habilitarSeleccion(true);
            vista.getMovimientosArea().setText(""); // Limpiar área de movimientos
            vista.getIniciar().setEnabled(true);     // Permitir iniciar de nuevo
            vista.getDetener().setEnabled(false);
            vista.getContinuar().setEnabled(false);
            vista.getReiniciar().setEnabled(false);
            vista.setTableroGenerado(true);
            pasoActual = 0;  // Reiniciar el paso actual
            this.recorridoActual = null;
        }
        private void guardarPartidaEnBD(ArrayList<int[]> recorrido) {
            Session session = null;
            Transaction tx = null;

            try {
                session = HibernateUtil.getSessionFactory().openSession();
                tx = session.beginTransaction();

                CaballoPartida partida = new CaballoPartida();

                for (int i = 0; i < recorrido.size(); i++) {
                    int[] pos = recorrido.get(i);
                    CaballoMovimiento movimiento = new CaballoMovimiento();
                    movimiento.setPaso(i + 1);
                    movimiento.setX(pos[0]);
                    movimiento.setY(pos[1]);
                    movimiento.setPartida(partida);
                    partida.getMovimientos().add(movimiento);
                }

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
        public void mostrarDatosEnGUI() {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                List<CaballoPartida> partidas = session.createQuery("FROM CaballoPartida ", CaballoPartida.class).list();

                StringBuilder sb = new StringBuilder();
                sb.append("=== HISTORIAL ===\n\n");

                for (CaballoPartida p : partidas) {
                    sb.append("Partida #").append(p.getId())
                            .append(" - ").append(p.getFecha()).append("\n");

                    for (CaballoMovimiento m : p.getMovimientos()) {
                        sb.append("  Paso ").append(m.getPaso())
                                .append(": (").append(m.getX())
                                .append(",").append(m.getY()).append(")\n");
                    }
                    sb.append("\n");
                }

                vista.getHistorialArea().setText(sb.toString()); // Mostrar en el área de historial
            } catch (Exception e) {
                JOptionPane.showMessageDialog(vista,
                        "Error al cargar datos: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

    }

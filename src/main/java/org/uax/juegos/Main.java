package org.uax.juegos;
import org.uax.juegos.vista.VentanaPrincipal;
import javax.swing.SwingUtilities;
public class Main
{
    public static void main( String[] args )
    {
        // Ejecutar en el hilo de eventos de Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaPrincipal().setVisible(true);
            }
        });
    }
}

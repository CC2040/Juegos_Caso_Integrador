package org.uax.juegos.modelo;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;


public class Caballo extends Figura{
    private ArrayList<int []> posiciones;
    // Constructor
    public Caballo(int movimientos,String nombre,ArrayList<int[]> posiciones) {
        super(movimientos,nombre);
        this.posiciones=posiciones;
    }

    // Getters y Setters
    public ArrayList<int[]> getPosiciones() {
        return posiciones;
    }
    public void setPosiciones(int [] posicion) {
        this.posiciones.add(posicion);
    }



    // Otros métodos
    private int contarMovimientosValidos(int x, int y, int[][] tablero, int[][] movimientos, int filas, int columnas) {
        int count = 0;
        for (int[] mov : movimientos) {
            int nx = x + mov[0];
            int ny = y + mov[1];
            if (esValido(nx, ny, tablero, filas, columnas)) {
                count++;
            }
        }
        return count;
    }

    public ArrayList<int[]> resolverCaballo(int filas, int columnas) {
        int[][] tablero = new int[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                tablero[i][j] = -1;
            }
        }

        int[] inicio = posiciones.get(0);
        int x = inicio[0];
        int y = inicio[1];

        int[][] movimientos = {
                {2, 1}, {1, 2}, {-1, 2}, {-2, 1},
                {-2, -1}, {-1, -2}, {1, -2}, {2, -1}
        };

        ArrayList<int[]> recorrido = new ArrayList<>();
        tablero[x][y] = 0;
        recorrido.add(new int[]{x, y});

        if (resolver(x, y, 1, tablero, movimientos, filas, columnas, recorrido)) {
            return recorrido;
        }

        return new ArrayList<>(); // no se pudo resolver
    }

    private boolean resolver(int x, int y, int paso, int[][] tablero, int[][] movimientos, int filas, int columnas, ArrayList<int[]> recorrido) {
        if (paso == filas * columnas) return true;

        List<int[]> siguientes = new ArrayList<>();
        for (int[] mov : movimientos) {
            int nuevoX = x + mov[0];
            int nuevoY = y + mov[1];
            if (esValido(nuevoX, nuevoY, tablero, filas, columnas)) {
                int grado = contarMovimientosValidos(nuevoX, nuevoY, tablero, movimientos, filas, columnas);
                siguientes.add(new int[]{nuevoX, nuevoY, grado});
            }
        }

        // Ordenar según la heurística de Warnsdorff
        siguientes.sort(Comparator.comparingInt(a -> a[2]));

        for (int[] siguiente : siguientes) {
            int nuevoX = siguiente[0];
            int nuevoY = siguiente[1];

            tablero[nuevoX][nuevoY] = paso;
            recorrido.add(new int[]{nuevoX, nuevoY});

            if (resolver(nuevoX, nuevoY, paso + 1, tablero, movimientos, filas, columnas, recorrido)) {
                return true;
            }

            // backtrack
            tablero[nuevoX][nuevoY] = -1;
            recorrido.remove(recorrido.size() - 1);
        }

        return false;
    }


    private boolean esValido(int x, int y, int[][] tablero, int filas, int columnas) {
        return x >= 0 && x < filas && y >= 0 && y < columnas && tablero[x][y] == -1;
    }

    @Override
    public void movimientoFigura(){
        System.out.println(movimientos);
    }

}


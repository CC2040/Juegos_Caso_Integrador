package org.uax.juegos.modelo.dominio;

import java.util.ArrayList;
import java.util.List;


public class Reina {
    private int n;
    private int[] tablero;

    public Reina(int n) {
        this.n = n;
        this.tablero = new int[n];
    }

    public List<int[]> resolver() {
        List<int[]> soluciones = new ArrayList<>();
        backtracking(0, soluciones);
        return soluciones;
    }

    private void backtracking(int fila, List<int[]> soluciones) {
        if (fila == n) {
            soluciones.add(tablero.clone());
            return;
        }
        for (int col = 0; col < n; col++) {
            if (esSeguro(fila, col)) {
                tablero[fila] = col;
                backtracking(fila + 1, soluciones);
            }
        }
    }

    private boolean esSeguro(int fila, int col) {
        for (int i = 0; i < fila; i++) {
            if (tablero[i] == col || Math.abs(tablero[i] - col) == Math.abs(i - fila))
                return false;
        }
        return true;
    }
}

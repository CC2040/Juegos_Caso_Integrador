package org.uax.juegos.modelo;

import java.util.Stack;
import java.util.function.BiConsumer;

public class Hanoi {
    private int numDiscos;
    private Stack<Integer>[] torres;

    public Hanoi(int numDiscos) {
        this.numDiscos = numDiscos;
        torres = new Stack[3];
        for (int i = 0; i < 3; i++) {
            torres[i] = new Stack<>();
        }
    }

    public void resolver(int n, int origen, int destino, int auxiliar, BiConsumer<Integer, Integer> accion) {
        if (n == 1) {
            int disco = torres[origen].pop();
            torres[destino].push(disco);
            accion.accept(origen, destino);
        } else {
            resolver(n - 1, origen, auxiliar, destino, accion);
            resolver(1, origen, destino, auxiliar, accion);
            resolver(n - 1, auxiliar, destino, origen, accion);
        }
    }

    public Stack<Integer>[] getTorres() {
        return torres;
    }
}

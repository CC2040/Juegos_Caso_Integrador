package org.uax.juegos.juegos;

import java.util.Stack;

public class Hanoi {
    private int numDiscos;
    private Stack<Integer>[] torres;

    public Hanoi(int numDiscos) {
        this.numDiscos = numDiscos;
        torres = new Stack[3];
        for (int i = 0; i < 3; i++) {
            torres[i] = new Stack<>();
        }

        for (int i = numDiscos; i >= 1; i--) {
            torres[0].push(i);
        }
    }

    public Stack<Integer>[] getTorres() {
        return torres;
    }

    public void resolver(int n, int origen, int destino, int auxiliar, HanoiMoveListener listener) {
        if (n > 0) {
            resolver(n - 1, origen, auxiliar, destino, listener);
            int disco = torres[origen].pop();
            torres[destino].push(disco);
            listener.onMove(origen, destino);
            resolver(n - 1, auxiliar, destino, origen, listener);
        }
    }

    public interface HanoiMoveListener {
        void onMove(int from, int to);
    }
}

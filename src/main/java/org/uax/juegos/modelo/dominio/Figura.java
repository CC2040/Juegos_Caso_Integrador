package org.uax.juegos.modelo.dominio;

public abstract class Figura {
    protected int movimientos;
    protected String nombre;

    public Figura(int movimientos, String nombre) {
        this.movimientos = movimientos;
        this.nombre = nombre;
    }

    // Getters y Setters
    public int getMovimientos() {
        return movimientos;
    }
    public void setMovimientos(int movimientos) {
        this.movimientos = movimientos;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    // Otros métodos
    public abstract void movimientoFigura();
}


package org.uax.juegos.modelo.persistencia;

import jakarta.persistence.*;

@Entity
@Table(name = "caballo_movimientos")
public class CaballoMovimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paso", nullable = false)
    private int paso;

    @Column(name = "coordenada_x", nullable = false)
    private int x;

    @Column(name = "coordenada_y", nullable = false)
    private int y;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partida_id", nullable = false)
    private CaballoPartida partida;

    // Constructor, Getters y Setters
    public CaballoMovimiento() {}

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public int getPaso() {
        return paso;
    }

    public void setPaso(int paso) {
        this.paso = paso;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public CaballoPartida getPartida() {
        return partida;
    }

    public void setPartida(CaballoPartida partida) {
        this.partida = partida;
    }
}
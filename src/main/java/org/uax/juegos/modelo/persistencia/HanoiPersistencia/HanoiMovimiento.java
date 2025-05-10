package org.uax.juegos.modelo.persistencia.HanoiPersistencia;

import jakarta.persistence.*;

@Entity
@Table(name = "Hanoi_Movimiento")
public class HanoiMovimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int movimiento;

    private String detalle;

    @ManyToOne
    @JoinColumn(name = "partida_id")
    private HanoiPartida partida;

    public HanoiMovimiento() {
    }

    public HanoiMovimiento(int movimiento, String detalle, HanoiPartida partida) {
        this.movimiento = movimiento;
        this.detalle = detalle;
        this.partida = partida;
    }

    public Long getId() {
        return id;
    }

    public int getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(int movimiento) {
        this.movimiento = movimiento;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public HanoiPartida getPartida() {
        return partida;
    }

    public void setPartida(HanoiPartida partida) {
        this.partida = partida;
    }
}
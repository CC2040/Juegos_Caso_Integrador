package org.uax.juegos.modelo.persistencia.ReinaPersistencia;

import jakarta.persistence.*;

@Entity
@Table(name = "reina_movimientos")
public class ReinaMovimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String solucion; // Solución en formato de texto

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partida_id", nullable = false)
    private ReinaPartida partida;

    public ReinaMovimiento() {}

    public ReinaMovimiento(String solucion, ReinaPartida partida) {
        this.solucion = solucion;
        this.partida = partida;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public String getSolucion() {
        return solucion;
    }

    public void setSolucion(String solucion) {
        this.solucion = solucion;
    }

    public ReinaPartida getPartida() {
        return partida;
    }

    public void setPartida(ReinaPartida partida) {
        this.partida = partida;
    }
}
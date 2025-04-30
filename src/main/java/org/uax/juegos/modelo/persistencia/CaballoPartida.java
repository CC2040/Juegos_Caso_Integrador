package org.uax.juegos.modelo.persistencia;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "caballo_partidas")
public class CaballoPartida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @OneToMany(mappedBy = "partida", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CaballoMovimiento> movimientos = new ArrayList<>();

    // Constructor, Getters y Setters
    public CaballoPartida() {
        this.fecha = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public List<CaballoMovimiento> getMovimientos() {
        return movimientos;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setMovimientos(List<CaballoMovimiento> movimientos) {
        this.movimientos = movimientos;
    }
}
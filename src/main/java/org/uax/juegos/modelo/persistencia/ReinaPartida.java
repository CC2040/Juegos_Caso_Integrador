package org.uax.juegos.modelo.persistencia;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "reina_partidas")
public class ReinaPartida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @OneToMany(mappedBy = "partida", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReinaMovimiento> movimientos;

    public ReinaPartida() {
        this.fecha = new Date();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public Date getFecha() {
        return fecha;
    }

    public List<ReinaMovimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<ReinaMovimiento> movimientos) {
        this.movimientos = movimientos;
    }
}
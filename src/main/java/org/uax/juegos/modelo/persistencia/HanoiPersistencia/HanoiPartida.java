package org.uax.juegos.modelo.persistencia.HanoiPersistencia;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Hanoi_Partida")
public class HanoiPartida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @OneToMany(mappedBy = "partida", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HanoiMovimiento> movimientos;

    public HanoiPartida() {
        this.fecha = new Date();
    }

    public Long getId() {
        return id;
    }

    public Date getFecha() {
        return fecha;
    }

    public List<HanoiMovimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<HanoiMovimiento> movimientos) {
        this.movimientos = movimientos;
    }
}
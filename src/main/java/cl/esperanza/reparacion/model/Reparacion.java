package cl.esperanza.reparacion.model;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reparacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reparacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_incidencia", nullable = false)
    private Integer idIncidencia;

    @Column(nullable = false)
    private LocalDate fechaReparacion;

    @Column(nullable = false)
    private String nombreOperador;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private String materialUtilizado; 

    @Column(nullable = false)
    private int cantidadUtilizada;

    @Column(nullable = false)
    private int costoManoObra;

    @Column(nullable = false)
    private int costoTotal;
}
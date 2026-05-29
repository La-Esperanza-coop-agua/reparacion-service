package cl.esperanza.reparacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.NotNull;
import cl.esperanza.reparacion.model.Reparacion;
import java.text.SimpleDateFormat;
import java.util.Date;

public record CreateReparacionRequest(
    @NotBlank(message = "El nombre del operador es obligatorio")
    String nombreOperador,

    @NotBlank(message = "La descripción es obligatoria")
    String descripcion,

    @NotNull(message = "El ID del material es obligatorio")
    Integer idMaterial,

    @PositiveOrZero(message = "La cantidad utilizada no puede ser negativa")
    int cantidadUtilizada,

    @PositiveOrZero(message = "El costo de mano de obra no puede ser negativo")
    int costoManoObra
) {
    public Reparacion toEntity() {
        Reparacion reparacion = new Reparacion();
        reparacion.setNombreOperador(this.nombreOperador());
        reparacion.setDescripcion(this.descripcion());
        reparacion.setCantidadUtilizada(this.cantidadUtilizada());
        reparacion.setCostoManoObra(this.costoManoObra());
        
        SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
        reparacion.setFechaReparacion(formateador.format(new Date()));
        
        return reparacion;
    }
}
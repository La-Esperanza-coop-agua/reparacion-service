package cl.esperanza.reparacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateReparacionRequest(
    @NotNull(message = "El ID de la incidencia es obligatorio")
    Integer idIncidencia,

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
) {}
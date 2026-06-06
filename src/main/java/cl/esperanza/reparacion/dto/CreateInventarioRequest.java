package cl.esperanza.reparacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateInventarioRequest(
    @NotBlank(message = "El nombre del material es obligatorio")
    String nombreMaterial,

    @PositiveOrZero(message = "La cantidad no puede ser negativa")
    int cantidadDisponible,

    @PositiveOrZero(message = "El precio unitario no puede ser negativo")
    int precioUnitario
) {}
package cl.esperanza.reparacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import cl.esperanza.reparacion.model.Inventario;

public record CreateInventarioRequest(
    @NotBlank(message = "El nombre del material es obligatorio")
    String nombreMaterial,

    @PositiveOrZero(message = "La cantidad no puede ser negativa")
    int cantidadDisponible,

    @PositiveOrZero(message = "El precio unitario no puede ser negativo")
    int precioUnitario
) {
    public Inventario toEntity() {
        Inventario inventario = new Inventario();
        inventario.setNombreMaterial(this.nombreMaterial());
        inventario.setCantidadDisponible(this.cantidadDisponible());
        inventario.setPrecioUnitario(this.precioUnitario());
        return inventario;
    }
}
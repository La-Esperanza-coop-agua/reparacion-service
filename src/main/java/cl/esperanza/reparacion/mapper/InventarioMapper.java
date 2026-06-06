package cl.esperanza.reparacion.mapper;

import cl.esperanza.reparacion.dto.CreateInventarioRequest;
import cl.esperanza.reparacion.model.Inventario;

public class InventarioMapper {
    public static Inventario toModel(CreateInventarioRequest request) {
        return new Inventario(
            null,
            request.nombreMaterial(),
            request.cantidadDisponible(),
            request.precioUnitario()
        );
    }
}
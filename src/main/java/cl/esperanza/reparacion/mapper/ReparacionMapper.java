package cl.esperanza.reparacion.mapper;

import java.time.LocalDate;
import cl.esperanza.reparacion.dto.CreateReparacionRequest;
import cl.esperanza.reparacion.model.Reparacion;

public class ReparacionMapper {
    public static Reparacion toModel(CreateReparacionRequest request) {
        return new Reparacion(
            null,
            request.idIncidencia(),
            LocalDate.now(),
            request.nombreOperador(),
            request.descripcion(),
            null,
            request.cantidadUtilizada(),
            request.costoManoObra(),
            0
        );
    }
}
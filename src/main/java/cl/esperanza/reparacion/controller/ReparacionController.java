package cl.esperanza.reparacion.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import cl.esperanza.reparacion.dto.*;
import cl.esperanza.reparacion.model.*;
import cl.esperanza.reparacion.service.ReparacionService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reparacion")
public class ReparacionController {

    private final ReparacionService reparacionService;
    private final WebClient incidenciasWebClient;

    public ReparacionController(ReparacionService reparacionService, 
                                @Qualifier("incidenciasWebClient") WebClient incidenciasWebClient) {
        this.reparacionService = reparacionService;
        this.incidenciasWebClient = incidenciasWebClient;
    }

    @PostMapping("/inventario")
    public ResponseEntity<Inventario> agregarMaterial(@Valid @RequestBody CreateInventarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reparacionService.registrarMaterial(request.toEntity()));
    }

    @GetMapping("/inventario")
    public ResponseEntity<List<Inventario>> obtenerInventario() {
        return ResponseEntity.ok(reparacionService.verInventario());
    }

    @PostMapping("/registrar")
    public ResponseEntity<Reparacion> registrarReparacion(@Valid @RequestBody CreateReparacionRequest request) {
        Reparacion nuevaReparacion = reparacionService.registrarReparacion(request.toEntity(), request.idMaterial());
        
        try {
            incidenciasWebClient.patch()
                .uri("/{id}/estado", request.idIncidencia())
                .bodyValue(new UpdateEstadoIncidenciaRequest(true))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        } catch (Exception e) {
            System.err.println("Error al conectar con Incidencias: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReparacion);
    }

    @GetMapping("/historial")
    public ResponseEntity<List<Reparacion>> obtenerHistorialReparaciones() {
        return ResponseEntity.ok(reparacionService.verHistorialReparaciones());
    }

    @GetMapping("/total-costos")
    public ResponseEntity<Double> getTotalCostos() {
        return ResponseEntity.ok(reparacionService.obtenerTotalCostos());
    }
}
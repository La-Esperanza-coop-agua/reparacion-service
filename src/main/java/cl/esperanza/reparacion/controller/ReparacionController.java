package cl.esperanza.reparacion.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import cl.esperanza.reparacion.dto.CreateInventarioRequest;
import cl.esperanza.reparacion.dto.CreateReparacionRequest;
import cl.esperanza.reparacion.dto.UpdateEstadoIncidenciaRequest;
import cl.esperanza.reparacion.mapper.InventarioMapper;
import cl.esperanza.reparacion.mapper.ReparacionMapper;
import cl.esperanza.reparacion.model.Inventario;
import cl.esperanza.reparacion.model.Reparacion;
import cl.esperanza.reparacion.service.ReparacionService;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/reparaciones")
@Tag(name = "Reparaciones e Inventario", description = "Gestión de los materiales y el registro de reparaciones en terreno.")
public class ReparacionController {

    private final ReparacionService reparacionService;
    private final WebClient incidenciasWebClient;

    public ReparacionController(ReparacionService reparacionService, WebClient incidenciasWebClient) {
        this.reparacionService = reparacionService;
        this.incidenciasWebClient = incidenciasWebClient; 
    }

    @Operation(summary = "Agregar material al inventario", description = "Registra un nuevo ítem en bodega.")
    @PostMapping("/inventario")
    public ResponseEntity<Inventario> agregarMaterial(@Valid @RequestBody CreateInventarioRequest request) {
        Inventario nuevoMaterial = reparacionService.registrarMaterial(InventarioMapper.toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMaterial);
    }

    @Operation(summary = "Obtener el inventario", description = "Lista todos los materiales disponibles y su stock.")
    @GetMapping("/inventario")
    public ResponseEntity<List<Inventario>> obtenerInventario() {
        return ResponseEntity.ok(reparacionService.verInventario());
    }

    @Operation(summary = "Registrar una nueva reparación", description = "Registra los datos de la reparación, descuenta stock del inventario y notifica al microservicio de Incidencias.")
    @PostMapping("/registrar")
    public ResponseEntity<Reparacion> registrarReparacion(@Valid @RequestBody CreateReparacionRequest request) {
        Reparacion reparacionEntity = ReparacionMapper.toModel(request);
        Reparacion nuevaReparacion = reparacionService.registrarReparacion(reparacionEntity, request.idMaterial());
        
        UpdateEstadoIncidenciaRequest updateRequest = new UpdateEstadoIncidenciaRequest(true);

        try {
            incidenciasWebClient.patch()
                .uri("/{id}/estado", request.idIncidencia()) 
                .bodyValue(updateRequest) 
                .retrieve()
                .bodyToMono(Void.class) 
                .block(); 
            System.out.println("Se actualizó la incidencia " + request.idIncidencia() + " a estadoReparacion=true");
        } catch (Exception e) {
            System.err.println("Error al conectar con la API de Incidencias: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReparacion);
    }

    @Operation(summary = "Obtener historial", description = "Lista todas las reparaciones históricas.")
    @GetMapping("/historial")
    public ResponseEntity<List<Reparacion>> obtenerHistorialReparaciones() {
        return ResponseEntity.ok(reparacionService.verHistorialReparaciones());
    }

    @Operation(summary = "Obtener total de costos", description = "Suma el valor total gastado en reparaciones (Mano de obra + Materiales). Usado por Reportes.")
    @GetMapping("/total-costos")
    public ResponseEntity<Double> getTotalCostos() {
        return ResponseEntity.ok(reparacionService.obtenerTotalCostos());
    }

    @Operation(summary = "Contar reparaciones pendientes", description = "Entrega la cantidad de reparaciones no finalizadas. Usado por Reportes para evaluación de fondos.")
    @GetMapping("/pendientes")
    public ResponseEntity<Long> getPendientes() {
        return ResponseEntity.ok(reparacionService.contarReparacionesPendientes());
    }
}

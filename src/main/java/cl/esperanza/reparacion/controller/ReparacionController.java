package cl.esperanza.reparacion.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

@RestController
@RequestMapping("/api/v1/reparacion")
public class ReparacionController {

    private final ReparacionService reparacionService;
    private final WebClient incidenciasWebClient;

    public ReparacionController(ReparacionService reparacionService, WebClient incidenciasWebClient) {
        this.reparacionService = reparacionService;
        this.incidenciasWebClient = incidenciasWebClient; 
    }

    @PostMapping("/inventario")
    public ResponseEntity<Inventario> agregarMaterial(@Valid @RequestBody CreateInventarioRequest request) {
        Inventario nuevoMaterial = reparacionService.registrarMaterial(InventarioMapper.toModel(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMaterial);
    }

    @GetMapping("/inventario")
    public ResponseEntity<List<Inventario>> obtenerInventario() {
        return ResponseEntity.ok(reparacionService.verInventario());
    }

    @PostMapping("/registrar")
    public ResponseEntity<Reparacion> registrarReparacion(@Valid @RequestBody CreateReparacionRequest request) {
        Reparacion reparacionEntity = ReparacionMapper.toModel(request);
        Reparacion nuevaReparacion = reparacionService.registrarReparacion(reparacionEntity, request.idMaterial());
        
        UpdateEstadoIncidenciaRequest updateRequest = new UpdateEstadoIncidenciaRequest(true);

        try {
            incidenciasWebClient.patch()
<<<<<<< HEAD
                .uri("/{id}/estado", request.idIncidencia()) 
                .bodyValue(updateRequest) 
                .retrieve()
                .bodyToMono(Void.class) 
                .block(); 
=======
                .uri("/{id}/estado", request.idIncidencia())
                .bodyValue(updateRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
                
>>>>>>> 52bf99765d90b498750eeda188801906930ac5a1
            System.out.println("Se actualizó la incidencia " + request.idIncidencia() + " a estadoReparacion=true");
        } catch (Exception e) {
            System.err.println("Error al conectar con la API de Incidencias: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReparacion);
    }

    @GetMapping("/historial")
    public ResponseEntity<List<Reparacion>> obtenerHistorialReparaciones() {
        return ResponseEntity.ok(reparacionService.verHistorialReparaciones());
    }
}
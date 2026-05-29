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
        this.incidenciasWebClient = incidenciasWebClient; // Inyeccion del webclient de incidencias
    }

    @PostMapping("/inventario")
    public ResponseEntity<Inventario> agregarMaterial(@Valid @RequestBody CreateInventarioRequest request) {
        Inventario nuevoMaterial = reparacionService.registrarMaterial(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMaterial);
    }

    @GetMapping("/inventario")
    public ResponseEntity<List<Inventario>> obtenerInventario() {
        return ResponseEntity.ok(reparacionService.verInventario());
    }


    @PostMapping("/registrar")
    public ResponseEntity<Reparacion> registrarReparacion(@Valid @RequestBody CreateReparacionRequest request) {
        Reparacion reparacionEntity = request.toEntity();
        Reparacion nuevaReparacion = reparacionService.registrarReparacion(reparacionEntity, request.idMaterial());
        
        UpdateEstadoIncidenciaRequest updateRequest = new UpdateEstadoIncidenciaRequest(true);

        try {
            incidenciasWebClient.put()
                .uri("/{id}", request.idIncidencia()) // Le pasa el ID a la URL: /api/v1/incidencias/5
                .bodyValue(updateRequest) // Envía el JSON con el estado "RESUELTA"
                .retrieve()
                .bodyToMono(Void.class) // Usamos Void porque no nos interesa leer el body de respuesta
                .block(); // .block() hace que sea síncrono, tal como el .block() de tu profesor
                
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
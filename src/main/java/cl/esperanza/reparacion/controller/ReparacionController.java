package cl.esperanza.reparacion.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import cl.esperanza.reparacion.model.Inventario;
import cl.esperanza.reparacion.model.Reparacion;
import cl.esperanza.reparacion.service.ReparacionService;
import cl.esperanza.reparacion.dto.CreateInventarioRequest;
import cl.esperanza.reparacion.dto.CreateReparacionRequest;

@RestController
@RequestMapping("/api/v1/reparacion")
public class ReparacionController {

    private final ReparacionService reparacionService;

    public ReparacionController(ReparacionService reparacionService) {
        this.reparacionService = reparacionService;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReparacion);
    }

    @GetMapping("/historial")
    public ResponseEntity<List<Reparacion>> obtenerHistorialReparaciones() {
        return ResponseEntity.ok(reparacionService.verHistorialReparaciones());
    }
}
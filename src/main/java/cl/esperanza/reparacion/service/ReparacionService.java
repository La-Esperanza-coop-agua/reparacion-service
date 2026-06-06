package cl.esperanza.reparacion.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.esperanza.reparacion.model.Inventario;
import cl.esperanza.reparacion.model.Reparacion;
import cl.esperanza.reparacion.repository.InventarioRepository;
import cl.esperanza.reparacion.repository.ReparacionRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReparacionService {

    @Autowired
    private ReparacionRepository reparacionRepo;

    @Autowired
    private InventarioRepository inventarioRepo;

    public Inventario registrarMaterial(Inventario inventario) {
        return inventarioRepo.save(inventario);
    }

    public Reparacion registrarReparacion(Reparacion reparacion, Integer idMaterial) {
        Inventario material = inventarioRepo.findById(idMaterial).orElse(null);

        if (material == null) {
            throw new RuntimeException("Error: No se encontró el material en la bodega con ID " + idMaterial);
        }

        if (material.getCantidadDisponible() < reparacion.getCantidadUtilizada()) {
            throw new RuntimeException("Error: No hay suficiente stock de " + material.getNombreMaterial());
        }

        int nuevoStock = material.getCantidadDisponible() - reparacion.getCantidadUtilizada();
        material.setCantidadDisponible(nuevoStock);
        inventarioRepo.save(material);
        int costoMateriales = reparacion.getCantidadUtilizada() * material.getPrecioUnitario();
        int total = costoMateriales + reparacion.getCostoManoObra();

        reparacion.setMaterialUtilizado(material.getNombreMaterial());
        reparacion.setCostoTotal(total);
        
        return reparacionRepo.save(reparacion);
    }

    public List<Inventario> verInventario() {
        return inventarioRepo.findAll();
    }

    public List<Reparacion> verHistorialReparaciones() {
        return reparacionRepo.findAll();
    }
}
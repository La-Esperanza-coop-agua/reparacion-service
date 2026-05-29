package cl.esperanza.reparacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.esperanza.reparacion.model.Inventario;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Integer> {
}
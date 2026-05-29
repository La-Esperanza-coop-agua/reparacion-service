package cl.esperanza.reparacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.esperanza.reparacion.model.Reparacion;
import java.util.List;

@Repository
public interface ReparacionRepository extends JpaRepository<Reparacion, Integer> {
    List<Reparacion> findByNombreOperador(String nombreOperador);
}
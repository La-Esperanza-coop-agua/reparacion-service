package cl.esperanza.reparacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // <-- IMPORTANTE: Agrega esta importación
import org.springframework.stereotype.Repository;
import cl.esperanza.reparacion.model.Reparacion;
import java.util.List;

@Repository
public interface ReparacionRepository extends JpaRepository<Reparacion, Integer> {
    
    List<Reparacion> findByNombreOperador(String nombreOperador);

    // Sumamos el campo 'costoTotal' que calcula tu ReparacionService de manera automatizada
    @Query("SELECT COALESCE(SUM(r.costoTotal), 0.0) FROM Reparacion r")
    double sumTotalCostosReparaciones();
}
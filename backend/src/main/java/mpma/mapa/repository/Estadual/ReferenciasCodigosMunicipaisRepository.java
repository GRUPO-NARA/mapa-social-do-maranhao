package mpma.mapa.repository.Estadual;

import mpma.mapa.entity.Estadual.ReferenciaCodigosMunicipaisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferenciasCodigosMunicipaisRepository extends JpaRepository<ReferenciaCodigosMunicipaisEntity, String> {
    @Query("SELECT r.municipio FROM ReferenciaCodigosMunicipaisEntity r")
    List<String> buscarMunicipiosDoEstado();
}

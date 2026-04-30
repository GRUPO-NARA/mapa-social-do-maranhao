package mpma.mapasocial.backend.repository.estadual;

import mpma.mapasocial.backend.entity.estadual.referenciasCodigosMunicipaisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface referenciasCodigosMunicipaisRepository extends JpaRepository<referenciasCodigosMunicipaisEntity, String> {
    @Query("SELECT r.municipio FROM referenciasCodigosMunicipaisEntity r")
    List<String> getMunicipios();
}

package mpma.mapasocial.backend.repository.geograficos;
import mpma.mapasocial.backend.entity.geograficos.AreaTerritorialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaTerritorialRepository extends JpaRepository<AreaTerritorialEntity, Long> {
    @Query(
            value = "SELECT at.valor FROM geograficos.area_territorial at " +
                    "JOIN dados_estadual.referencias_codigos_municipais i ON i.codigo_ibge = at.cod_municipio " +
                    "WHERE i.municipio = :municipio;"
    , nativeQuery = true)
    Double buscarAreaTerritoralPorMunicipio(@Param("municipio") String municipio);
}

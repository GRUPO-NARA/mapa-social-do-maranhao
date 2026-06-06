package mpma.mapa.repository.Geograficos;
import mpma.mapa.entity.Geograficos.AreaTerritorialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaTerritorialRepository extends JpaRepository<AreaTerritorialEntity, Long> {
    @Query(
            value = """
            SELECT json_build_object(
                               'Área Territorial', t.valor,
                               'Referência dos Dados', t.referencia,
                               'Fonte dos Dados', t.fonte
                        )
                        FROM geograficos.area_territorial as t
                        JOIN dados_estadual.referencias_codigos_municipais rcm on t.cod_municipio = rcm.codigo_ibge
                        WHERE rcm.municipio = :municipio
                        GROUP BY t.valor, t.referencia, t.fonte
                        ORDER BY t.referencia DESC
                        LIMIT 1;
            """
            ,nativeQuery = true
    )
    String buscarAreaTerritoralDoMunicipio(String municipio);
}

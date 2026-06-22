package mpma.mapa.repository.Saude;

import org.springframework.data.jpa.repository.JpaRepository;
import mpma.mapa.entity.Saude.CasosTurbeculoseEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CasosTuberculoseRepository extends JpaRepository<CasosTurbeculoseEntity, Long> {

    @Query(
            value = """
            SELECT json_build_object(
                                'Casos de Tuberculose', t.valor,
                                'Referência dos Dados', t.referencia,
                                'Fonte dos Dados', t.fonte
                         )
                         FROM saude.casos_turbeculose as t
                         JOIN dados_estadual.referencias_codigos_municipais rcm on t.cod_municipio = rcm.codigo_ibge
                         WHERE rcm.municipio = :municipio
                         GROUP BY t.valor, t.referencia, t.fonte
                         ORDER BY t.referencia DESC
                         LIMIT 1;
            """
            , nativeQuery = true
    )
    String buscarCasosTuberculoseDoMunicipio(@Param("municipio") String municipio);
}
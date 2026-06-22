package mpma.mapa.repository.Demograficos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mpma.mapa.entity.Demograficos.PopulacaoResidenteEmFavelaEntity;

import java.util.List;

@Repository
public interface PopulacaoResidenteEmFavelaRepository extends JpaRepository<PopulacaoResidenteEmFavelaEntity, Long> {

    @Query(
            value = """
            SELECT json_build_object(
                               'Quantidade de Pessoas em Favelas', t.valor,
                               'Referência dos Dados', t.referencia,
                               'Fonte dos Dados', t.fonte
                        )
                        FROM demograficos.populacao_residente_em_favelas as t
                        JOIN dados_estadual.referencias_codigos_municipais rcm on t.cod_municipio = rcm.codigo_ibge
                        WHERE rcm.municipio = ?1
                        GROUP BY t.valor, t.referencia, t.fonte
                        ORDER BY t.referencia DESC
                        LIMIT 1;
            """
            , nativeQuery = true
    )
    String buscarPopulacaoResidenteEmFavelaDoMunicipio(String municipio);

    @Query(value = """
            SELECT json_build_object(
                'Referência', t.referencia,
                'Quantidade de Pessoas em Favelas', t.valor
            )
            FROM demograficos.populacao_residente_em_favelas as t
            JOIN dados_estadual.referencias_codigos_municipais rcm on t.cod_municipio = rcm.codigo_ibge
            WHERE rcm.municipio = :municipio
            ORDER BY t.referencia ASC
            """, nativeQuery = true)
    List<String> buscarEvolucaoPopulacaoResidenteEmFavelaDoMunicipio(@Param("municipio") String municipio);

    @Query(
            value = """
            SELECT json_build_object(
                               'Quantidade de Pessoas em Favelas', t.valor,
                               'Referência dos Dados', t.referencia,
                               'Fonte dos Dados', t.fonte
                        )
                        FROM demograficos.populacao_residente_em_favelas as t
                        JOIN dados_estadual.referencias_codigos_municipais rcm on t.cod_municipio = rcm.codigo_ibge
                        WHERE rcm.municipio = :municipio
                          AND t.referencia = :referencia
                        GROUP BY t.valor, t.referencia, t.fonte
                        LIMIT 1;
            """
            , nativeQuery = true
    )
    String buscarPopulacaoResidenteEmFavelaDoMunicipioPorAno(@Param("municipio") String municipio, @Param("referencia") String referencia);
}

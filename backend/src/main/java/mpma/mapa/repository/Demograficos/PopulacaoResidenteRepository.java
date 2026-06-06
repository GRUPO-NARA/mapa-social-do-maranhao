package mpma.mapa.repository.Demograficos;

import org.springframework.data.jpa.repository.JpaRepository;
import mpma.mapa.entity.Demograficos.PopulacaoResidenteEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PopulacaoResidenteRepository extends JpaRepository<PopulacaoResidenteEntity, Long> {

    @Query(
            value = """
            SELECT json_build_object(
                               'Quantidade de Pessoas', t.valor,
                               'Referência dos Dados', t.referencia,
                               'Fonte dos Dados', t.fonte
                        )
                        FROM demograficos.populacao_residente as t
                        JOIN dados_estadual.referencias_codigos_municipais rcm on t.cod_municipio = rcm.codigo_ibge
                        WHERE rcm.municipio = ?1
                        GROUP BY t.valor, t.referencia, t.fonte
                        ORDER BY t.referencia DESC
                        LIMIT 1;
            """
            ,nativeQuery = true
    )
    String buscarPopulacaoTotalDoMunicipio(String municipio);

    @Query(
            value = """
            SELECT json_build_object(
                               'Quantidade de Pessoas', SUM(t.valor),
                               'Referência dos Dados', t.referencia,
                               'Fonte dos Dados', t.fonte
                        )
                        FROM demograficos.populacao_residente as t
                        GROUP BY t.referencia, t.fonte
                        ORDER BY t.referencia DESC
                        LIMIT 1;
                        
            """,
            nativeQuery = true
    )
    String buscarPopulacaoEstadualRecente();
}

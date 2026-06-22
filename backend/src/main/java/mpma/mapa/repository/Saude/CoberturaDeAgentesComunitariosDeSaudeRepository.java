package mpma.mapa.repository.Saude;

import mpma.mapa.entity.Saude.CoberturaDeAgentesComunitariosDeSaudeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CoberturaDeAgentesComunitariosDeSaudeRepository extends JpaRepository<CoberturaDeAgentesComunitariosDeSaudeEntity, Long> {

    @Query(
            value = """
            SELECT json_build_object(
                                'Cobertura de Agentes Comunitários de Saúde', t.valor,
                                'Referência dos Dados', t.referencia,
                                'Fonte dos Dados', t.fonte
                         )
                         FROM saude.cobertura_de_agentes_comunitarios_de_saude as t
                         JOIN dados_estadual.referencias_codigos_municipais rcm on t.cod_municipio = rcm.codigo_ibge
                         WHERE rcm.municipio = :municipio
                         GROUP BY t.valor, t.referencia, t.fonte
                         ORDER BY t.referencia DESC
                         LIMIT 1;
            """
            , nativeQuery = true
    )
    String buscarCoberturaDeAgentesComunitariosDeSaudeDoMunicipio(@Param("municipio") String municipio);
}
package mpma.mapa.repository.Educacao;

import mpma.mapa.entity.Educacao.TaxaDeAprovacaoNoEnsinoMedioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository

public interface TaxaDeAprovacaoNoEnsinoMedioRepository extends JpaRepository<TaxaDeAprovacaoNoEnsinoMedioEntity, Long> {
    @Query(
            value = """
                    SELECT json_build_object(
                                        'Taxa de Aprovação no Ensino Médio', t.valor,
                                        'Referência dos Dados', t.referencia,
                                        'Fonte dos Dados', t.fonte
                                 )
                                 FROM educacao.taxa_de_aprovacao_no_ensino_medio as t
                                 JOIN dados_estadual.referencias_codigos_municipais rcm on t.cod_municipio = rcm.codigo_ibge
                                 WHERE rcm.municipio = :municipio
                                 GROUP BY t.valor, t.referencia, t.fonte
                                 ORDER BY t.referencia DESC
                                 LIMIT 1;
                    """
            , nativeQuery = true
    )
    String buscarTaxaDeAprovacaoNoEnsinoMedioDoMunicipio(String municipio);
}

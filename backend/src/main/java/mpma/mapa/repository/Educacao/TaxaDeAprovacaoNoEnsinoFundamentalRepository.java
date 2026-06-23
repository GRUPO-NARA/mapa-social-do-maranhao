package mpma.mapa.repository.Educacao;

import mpma.mapa.entity.Educacao.TaxaDeAprovacaoNoEnsinoFundamentalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository

public interface TaxaDeAprovacaoNoEnsinoFundamentalRepository extends JpaRepository<TaxaDeAprovacaoNoEnsinoFundamentalEntity, Long> {
    @Query(
            value = """
                    SELECT json_build_object(
                                        'Taxa de Aprovação no Ensino Fundamental', t.valor,
                                        'Referência dos Dados', t.referencia,
                                        'Fonte dos Dados', t.fonte
                                 )
                                 FROM educacao.taxa_de_aprovacao_no_ensino_fundamental as t
                                 JOIN dados_estadual.referencias_codigos_municipais rcm on t.cod_municipio = rcm.codigo_ibge
                                 WHERE rcm.municipio = :municipio
                                 GROUP BY t.valor, t.referencia, t.fonte
                                 ORDER BY t.referencia DESC
                                 LIMIT 1;
                    """
            , nativeQuery = true
    )
    String buscarTaxaDeAprovacaoNoEnsinoFundamentalDoMunicipio(String municipio);
}

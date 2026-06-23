package mpma.mapa.repository.Educacao;

import mpma.mapa.entity.Educacao.TaxaDeAnalfabetismo15AnosOuMaisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository

public interface TaxaDeAnalfabetismo15AnosOuMaisRepository extends JpaRepository<TaxaDeAnalfabetismo15AnosOuMaisEntity, Long> {
    @Query(
            value = """
                    SELECT json_build_object(
                                        'Taxa de Analfabetismo (15 anos ou mais)', t.valor,
                                        'Referência dos Dados', t.referencia,
                                        'Fonte dos Dados', t.fonte
                                 )
                                 FROM educacao.taxa_de_analfabetismo_15_anos_ou_mais as t
                                 JOIN dados_estadual.referencias_codigos_municipais rcm on t.cod_municipio = rcm.codigo_ibge
                                 WHERE rcm.municipio = :municipio
                                 GROUP BY t.valor, t.referencia, t.fonte
                                 ORDER BY t.referencia DESC
                                 LIMIT 1;
                    """
            , nativeQuery = true
    )
    String buscarTaxaDeAnalfabetismo15AnosOuMaisDoMunicipio(String municipio);
}

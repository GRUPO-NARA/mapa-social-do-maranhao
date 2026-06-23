package mpma.mapa.repository.Educacao;

import mpma.mapa.entity.Educacao.TaxaDeAnalfabetismo15AnosOuMaisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // Adicionado import para segurança
import org.springframework.stereotype.Repository;

@Repository
public interface TaxaDeAnalfabetismo15AnosOuMaisRepository extends JpaRepository<TaxaDeAnalfabetismo15AnosOuMaisEntity, Long> {

    @Query(
            value = """
                    SELECT json_build_object(
                                        'Média da Taxa de Analfabetismo (15 anos ou mais)', ROUND(AVG(t.valor)::numeric, 2),
                                        'Referência dos Dados', 'Média Histórica',
                                        'Fonte dos Dados', t.fonte
                                 )
                                 FROM educacao.taxa_de_analfabetismo_15_anos_ou_mais as t
                                 JOIN dados_estadual.referencias_codigos_municipais rcm on t.cod_municipio = rcm.codigo_ibge
                                 WHERE rcm.municipio = :municipio
                                 GROUP BY t.fonte;
                    """
            , nativeQuery = true
    )

    String buscarMediaDaTaxaDeAnalfabetismoDoMunicipio(@Param("municipio") String municipio);
}
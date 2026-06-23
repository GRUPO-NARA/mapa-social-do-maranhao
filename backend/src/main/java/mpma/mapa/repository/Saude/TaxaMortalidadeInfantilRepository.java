package mpma.mapa.repository.Saude;

import mpma.mapa.entity.Saude.TaxaMortalidadeInfantilEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxaMortalidadeInfantilRepository extends JpaRepository<TaxaMortalidadeInfantilEntity, Long> {

    @Query(
            value = """
                    SELECT json_build_object(
                                        'Média da Taxa de Mortalidade Infantil', ROUND(AVG(t.valor)::numeric, 2),
                                        'Referência dos Dados', 'Média Histórica',
                                        'Fonte dos Dados', t.fonte
                                 )
                                 FROM saude.taxa_mortalidade_infantil as t
                                 JOIN dados_estadual.referencias_codigos_municipais rcm on t.cod_municipio = rcm.codigo_ibge
                                 WHERE rcm.municipio = :municipio
                                 GROUP BY t.fonte;
                    """
            , nativeQuery = true
    )
    String buscarMediaDaTaxaMortalidadeInfantilDoMunicipio(@Param("municipio") String municipio);
}
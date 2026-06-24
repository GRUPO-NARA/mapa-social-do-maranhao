package mpma.mapa.repository.Saude;

import mpma.mapa.entity.Saude.TaxaMortalidadeInfantilEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxaMortalidadeInfantilRepository extends JpaRepository<TaxaMortalidadeInfantilEntity, Long> {

    @Query(value = """
        SELECT json_build_object(
                        'Taxa de Mortalidade Infantil', t.valor,
                        'Referência dos Dados', t.referencia,
                        'Fonte dos Dados', t.fonte
                    )
            FROM saude.taxa_mortalidade_infantil as t
            JOIN dados_estadual.referencias_codigos_municipais rcm ON t.cod_municipio = rcm.codigo_ibge
            WHERE rcm.municipio = ?1
            ORDER BY t.referencia DESC
            LIMIT 1;""", nativeQuery = true
    )
    String buscarTaxaDeMortalidadeMunicipal(String municipio);

    @Query(value = """
        SELECT json_build_object(
                        'Taxa de Mortalidade Infantil', AVG(t.valor),
                        'Referência dos Dados', t.referencia,
                        'Fonte dos Dados', t.fonte
                    )
            FROM saude.taxa_mortalidade_infantil as t
            WHERE t.referencia = (SELECT MAX(referencia) FROM saude.taxa_mortalidade_infantil)
            GROUP BY t.referencia, t.fonte
            LIMIT 1;""", nativeQuery = true
    )
    String buscarTaxaDeMortalidadeEstadual();

}
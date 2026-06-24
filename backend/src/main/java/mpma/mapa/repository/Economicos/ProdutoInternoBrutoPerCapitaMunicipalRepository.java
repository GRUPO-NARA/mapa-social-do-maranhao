package mpma.mapa.repository.Economicos;

import mpma.mapa.entity.Economicos.ProdutoInternoBrutoPerCapitaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoInternoBrutoPerCapitaMunicipalRepository extends JpaRepository<ProdutoInternoBrutoPerCapitaEntity, Long> {

    @Query(
            value = """
            SELECT json_build_object(
                        'Produto Interno Bruto per Capita', t.valor,
                        'Referência dos Dados', t.referencia,
                        'Fonte dos Dados', t.fonte
                    )
            FROM economicos.pib_per_capita as t
            JOIN dados_estadual.referencias_codigos_municipais rcm ON t.cod_municipio = rcm.codigo_ibge
            WHERE rcm.municipio = ?1
            ORDER BY t.referencia DESC
            LIMIT 1;""",
            nativeQuery = true
    )
    String buscarProdutoInternoBrutoPerCapitaMunicipal(String municipio);

    @Query(
            value = """
            SELECT json_build_object(
                        'Produto Interno Bruto per Capita', AVG(t.valor),
                        'Referência dos Dados', t.referencia,
                        'Fonte dos Dados', t.fonte
                    )
            FROM economicos.pib_per_capita as t
            WHERE t.referencia = (SELECT MAX(referencia) FROM economicos.pib_per_capita)
            GROUP BY t.referencia, t.fonte
            LIMIT 1;""",
            nativeQuery = true
    )
    String buscarPibPerCapitaEstadual();
}

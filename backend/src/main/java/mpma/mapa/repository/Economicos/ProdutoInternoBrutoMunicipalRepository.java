package mpma.mapa.repository.Economicos;

import org.springframework.data.jpa.repository.JpaRepository;
import mpma.mapa.entity.Economicos.ProdutoInternoBrutoMunicipalEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProdutoInternoBrutoMunicipalRepository extends JpaRepository<ProdutoInternoBrutoMunicipalEntity, Long> {
    @Query(
            value = """
            SELECT json_build_object(
                               'Produto Interno Bruto', t.valor,
                               'Referência dos Dados', t.referencia,
                               'Fonte dos Dados', t.fonte
                        )
                        FROM economicos.produto_interno_bruto as t
                        JOIN dados_estadual.referencias_codigos_municipais rcm on t.cod_municipio = rcm.codigo_ibge
                        WHERE rcm.municipio = ?1
                        GROUP BY t.valor, t.referencia, t.fonte
                        ORDER BY t.referencia DESC
                        LIMIT 1;
            """,
            nativeQuery = true
    )
    String buscarProdutoInternoBrutoMunicipal(String municipio);

    @Query(
            value = """
            SELECT json_build_object(
                               'Produto Interno Bruto Agregado', SUM(t.valor),
                               'Referência dos Dados', t.referencia,
                               'Fonte dos Dados', t.fonte
                        )
                        FROM economicos.produto_interno_bruto as t
                        GROUP BY t.valor, t.referencia, t.fonte
                        ORDER BY t.referencia DESC
                        LIMIT 1;
            """,
            nativeQuery = true
    )
    String buscarProdutoInternoBrutoAgregadoEstadualRecente();


}

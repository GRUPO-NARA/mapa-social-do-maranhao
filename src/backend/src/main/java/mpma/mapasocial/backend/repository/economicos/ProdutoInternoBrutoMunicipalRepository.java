package mpma.mapasocial.backend.repository.economicos;

import org.springframework.data.jpa.repository.JpaRepository;
import mpma.mapasocial.backend.entity.economicos.produtoInternoBrutoMunicipalEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProdutoInternoBrutoMunicipalRepository extends JpaRepository<produtoInternoBrutoMunicipalEntity, Long> {
    @Query(value = "SELECT json_build_object(\n" +
            "            'referencia', pib.referencia,\n" +
            "            'PIB_municipio', pib.valor)\n" +
            "FROM economicos.produto_interno_bruto as pib\n" +
            "JOIN dados_estadual.referencias_codigos_municipais i on pib.cod_municipio = i.codigo_ibge\n" +
            "WHERE i.municipio = :municipio \n" +
            "GROUP BY pib.referencia, pib.valor\n" +
            "ORDER BY pib.referencia DESC\n" +
            "LIMIT 1;", nativeQuery = true)
    String buscarPIB(@Param("municipio") String municipio);

    @Query(value = "SELECT json_build_object(\n" +
            "       'referencia' , pib.referencia,\n" +
            "       'pib_agregado'      , SUM(pib.valor)\n" +
            "       )\n" +
            "FROM economicos.produto_interno_bruto as pib\n" +
            "GROUP BY pib.referencia\n" +
            "ORDER BY pib.referencia DESC", nativeQuery = true)
    String buscarPIBAgregadoRecente();
}

package mpma.mapasocial.backend.repository.economicos;

import org.springframework.data.jpa.repository.JpaRepository;
import mpma.mapasocial.backend.entity.economicos.produtoInternoBrutoMunicipalEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository para o indicador econômico de Produto Interno Bruto Municipal.
 *
 * Esta interface realiza consultas nativas no banco de dados, devolvendo JSON
 * construído diretamente pelo PostgreSQL via <code>json_build_object</code>.
 * Dessa forma, a aplicação recebe uma String JSON estruturada, reduzindo o
 * processamento de mapeamento na camada Java.
 */
public interface ProdutoInternoBrutoMunicipalRepository extends JpaRepository<produtoInternoBrutoMunicipalEntity, Long> {

    /**
     * Busca o PIB municipal mais recente para o município informado.
     *
     * A query utiliza <code>json_build_object</code> para construir uma String JSON
     * com as chaves <code>referencia</code> e <code>PIB_municipio</code> no próprio banco.
     * A ordenação decrescente por <code>pib.referencia</code> e o <code>LIMIT 1</code>
     * garantem que apenas a fotografia mais recente do PIB municipal seja retornada.
     *
     * @param municipio filtro de busca por texto para o nome do município
     * @return String estruturada como JSON contendo o PIB municipal mais recente
     */
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

    /**
     * Busca o PIB agregado mais recente como JSON estruturado pelo banco.
     *
     * A query usa <code>json_build_object</code> para produzir uma String JSON com
     * as chaves <code>referencia</code> e <code>pib_agregado</code>, permitindo
     * que a aplicação consuma diretamente o resultado como JSON.
     *
     * @return String estruturada como JSON com o PIB agregado mais recente
     */
    @Query(value = "SELECT json_build_object(\n" +
            "       'referencia' , pib.referencia,\n" +
            "       'pib_agregado'      , SUM(pib.valor)\n" +
            "       )\n" +
            "FROM economicos.produto_interno_bruto as pib\n" +
            "GROUP BY pib.referencia\n" +
            "ORDER BY pib.referencia DESC", nativeQuery = true)
    String buscarPIBAgregadoRecente();
}

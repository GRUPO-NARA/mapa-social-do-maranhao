package mpma.mapasocial.backend.repository.demograficos;

import org.springframework.data.jpa.repository.JpaRepository;
import mpma.mapasocial.backend.entity.demograficos.populacaoResidenteEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository para indicadores demográficos relacionados à população residente.
 *
 * Esta interface representa a persistência dos dados de população e permite consultas
 * tanto de valores agregados por município quanto de JSON nativo retornado pelo PostgreSQL.
 */
@Repository
public interface PopulacaoResidenteRepository extends JpaRepository<populacaoResidenteEntity, Long> {

    /**
     * Busca o valor absoluto da população residente para o município informado.
     *
     * @param municipio filtro de busca por texto para o nome do município
     * @return Long valor da população residente do município
     */
    @Query(value = "SELECT pr.valor FROM demograficos.populacao_residente pr " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON pr.cod_municipio = i.codigo_ibge " +
            "WHERE i.municipio ILIKE :municipio ",
            nativeQuery = true)
    Long buscarPopulacaoResidente(@Param("municipio") String municipio);

    /**
     * Busca a população estadual mais recente como JSON estruturado pelo banco.
     *
     * A query utiliza <code>json_build_object</code> do PostgreSQL para produzir diretamente
     * uma String JSON com as chaves <code>referencia</code> e <code>populacao_estado</code>,
     * o que otimiza o mapeamento na camada Java.
     *
     * @return String estruturada como JSON com o dado estadual mais recente
     */
    @Query(value = "SELECT json_build_object(\n" +
            "       'referencia', pr.referencia,\n" +
            "       'populacao_estado', SUM(pr.valor)\n" +
            "       )\n" +
            "FROM demograficos.populacao_residente as pr\n" +
            "GROUP BY pr.referencia\n" +
            "ORDER BY pr.referencia DESC\n" +
            "LIMIT 1;\n" +
            "\n", nativeQuery = true)
    String buscaPopulacaoResidenteEstadualDosDadosMaisRecentes();
}

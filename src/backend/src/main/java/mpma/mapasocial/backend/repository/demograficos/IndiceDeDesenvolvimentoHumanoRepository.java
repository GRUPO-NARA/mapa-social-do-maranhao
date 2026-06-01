package mpma.mapasocial.backend.repository.demograficos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mpma.mapasocial.backend.entity.demograficos.indiceDeDesenvolvimentoHumanoEntity;

import java.util.List;

/**
 * Repository para o indicador de Índice de Desenvolvimento Humano (IDH) na camada Demográficos.
 *
 * Esta interface define consultas nativas PostgreSQL que retornam JSON construído
 * diretamente pelo banco de dados usando <code>json_build_object</code>, reduzindo o
 * trabalho de mapeamento na camada de aplicação Java.
 */
@Repository
public interface IndiceDeDesenvolvimentoHumanoRepository extends JpaRepository<indiceDeDesenvolvimentoHumanoEntity, Long> {

    /**
     * Busca o índice de desenvolvimento humano municipal mais recente.
     *
     * A query utiliza <code>json_build_object</code> do PostgreSQL para construir uma string
     * JSON contendo as chaves <code>IDH_municipal</code> e <code>referencia</code> no próprio banco.
     * Assim, a camada Java recebe diretamente uma String estruturada em JSON, otimizando o mapeamento.
     *
     * A ordenação decrescente por <code>idh.referencia</code> e o <code>LIMIT 1</code> asseguram que
     * seja retornada estritamente a fotografia do IDH municipal mais recente mapeada pelas fontes oficiais.
     *
     * @param municipio filtro de busca por texto para o nome do município
     * @return String estruturada como JSON com o IDH municipal mais recente
     */
    @Query(value = "SELECT json_build_object(\n" +
            "               'IDH_municipal', idh.valor,\n" +
            "               'referencia', idh.referencia\n" +
            "       ) AS resultado\n" +
            "\n" +
            "FROM demograficos.indice_de_desenvolvimento_humano idh\n" +
            "JOIN dados_estadual.referencias_codigos_municipais i ON i.codigo_ibge = idh.cod_municipio\n" +
            "WHERE i.municipio = :municipio \n" +
            "GROUP BY idh.valor, idh.referencia\n" +
            "ORDER BY idh.referencia DESC\n" +
            "LIMIT 1;", nativeQuery = true)
    String buscarIndiceDeDesenvolvimentoHumanoPorMunicipio(@Param("municipio") String municipio);

    /**
     * Busca a evolução histórica do IDH para o município informado.
     *
     * A query usa <code>json_build_object</code> para retornar cada ano como uma String JSON
     * contendo <code>referencia</code> e <code>IDH</code>. Isso permite carregar diretamente
     * no frontend uma série histórica pronta para gráficos.
     *
     * Este método devolve uma <code>List<String></code> porque seu objetivo é trazer toda a série
     * histórica de evolução do IDH ao longo de todos os anos disponíveis no banco.
     *
     * @param municipio filtro de busca por texto para o nome do município
     * @return List<String> onde cada elemento é uma String estruturada como JSON representando um ano da série histórica
     */
    @Query(value = "SELECT\n" +
            "    json_build_object(\n" +
            "        'referencia', idh.referencia,\n" +
            "        'IDH', idh.valor\n" +
            "    )\n" +
            "FROM demograficos.indice_de_desenvolvimento_humano as idh\n" +
            "JOIN dados_estadual.referencias_codigos_municipais as i ON i.codigo_ibge = idh.cod_municipio\n" +
            "WHERE i.municipio = :municipio", nativeQuery = true)
    List<String> buscarEvolucaoIndiceDeDesenvolvimentoHumanoPorMunicipio(@Param("municipio") String municipio);
}

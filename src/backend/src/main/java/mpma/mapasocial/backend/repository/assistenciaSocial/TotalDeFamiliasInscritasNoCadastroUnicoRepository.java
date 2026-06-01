package mpma.mapasocial.backend.repository.assistenciaSocial;

import mpma.mapasocial.backend.entity.assistenciaSocial.totalDeFamiliasInscritasNoCadastroUnicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository para o indicador de total de famílias inscritas no Cadastro Único.
 *
 * Esta interface garante o acesso ao dado mais recente por município para o indicador
 * de assistênca social, evitando a exposição de registros históricos defasados.
 */
@Repository
public interface TotalDeFamiliasInscritasNoCadastroUnicoRepository extends JpaRepository<totalDeFamiliasInscritasNoCadastroUnicoEntity, Long> {

    /**
     * Busca o total de famílias inscritas no Cadastro Único para o município informado.
     *
     * A query nativa ordena por referência descendente e limita o resultado a 1 para
     * retornar a fotografia mais recente disponível no banco de dados.
     *
     * @param municipio nome do município usado na pesquisa
     * @return Long valor do indicador no registro mais recente encontrado
     */
    @Query(value = "SELECT tf.valor FROM assistencia_social.total_de_familias_inscritas_no_cadastro_unico tf " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON tf.cod_municipio = i.codigo_ibge " +
            "WHERE i.municipio ILIKE :municipio " +
            "ORDER BY tf.referencia DESC " +
            "LIMIT 1",
            nativeQuery = true)
    Long buscarTotalDeFamiliasInscritasNoCadastroUnico(@Param("municipio") String municipio);
}
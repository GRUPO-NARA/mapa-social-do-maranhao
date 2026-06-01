package mpma.mapasocial.backend.repository.assistenciaSocial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mpma.mapasocial.backend.entity.assistenciaSocial.familiasEmSituacaoDeRuaInscritasCadastroUnicoEntity;

import java.util.Optional;

/**
 * Repository para o indicador de famílias em situação de rua inscritas no Cadastro Único.
 *
 * Esta interface representa a camada de acesso a dados para o indicador de assistência social
 * que mapeia registros históricos por município e garante a seleção do dado mais recente.
 */
@Repository
public interface FamiliasEmSituacaoDeRuaInscritasCadastroUnicoRepository extends JpaRepository<familiasEmSituacaoDeRuaInscritasCadastroUnicoEntity, Long> {

    /**
     * Busca a quantidade de famílias em situação de rua inscritas no Cadastro Único para o município informado.
     *
     * A query nativa utiliza a junção com a tabela de referências de códigos de municípios
     * e aplica <code>ORDER BY f.referencia DESC LIMIT 1</code> para ignorar múltiplos anos
     * e retornar sempre o registro mais recente disponível no banco de dados.
     *
     * @param municipio nome do município usado na pesquisa
     * @return Optional<Long> valor do indicador no registro mais recente encontrado
     */
    @Query(value = "SELECT f.valor FROM assistencia_social.familias_em_situacao_de_rua_inscritas_cadastro_unico f " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON f.cod_municipio = i.codigo_ibge " +
            "WHERE i.municipio ILIKE :municipio " +
            "ORDER BY f.referencia DESC " +
            "LIMIT 1", nativeQuery = true)
    Optional<Long> buscarQuantidadeFamiliasRuaPorMunicipio(@Param("municipio") String municipio);
}

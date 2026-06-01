package mpma.mapasocial.backend.repository.assistenciaSocial;

import mpma.mapasocial.backend.entity.assistenciaSocial.familiasEmSituacaoDeTrabalhoInfantilCadastroUnicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository para o indicador de famílias em situação de trabalho infantil inscritas no Cadastro Único.
 *
 * Esta interface encapsula a busca de dados por município e assegura que apenas o registro
 * mais recente seja devolvido para evitar duplicidades em diferentes anos de referência.
 */
@Repository
public interface FamiliasEmSituacaoDeTrabalhoInfantilCadastroUnicoRepository extends JpaRepository<familiasEmSituacaoDeTrabalhoInfantilCadastroUnicoEntity, Long> {
    /**
     * Busca a quantidade de famílias em situação de trabalho infantil inscritas no Cadastro Único para o município informado.
     *
     * A query nativa ordena por referência decrescente e limita o resultado a 1 para retornar
     * sempre a fotografia mais recente do banco, ignorando registros de anos anteriores.
     *
     * @param municipio nome do município usado na pesquisa
     * @return Optional<Long> valor do indicador no registro mais recente encontrado
     */
    @Query(value = "SELECT fsti.valor FROM assistencia_social.familias_em_situacao_de_trabalho_infantil_cadastro_unico fsti " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON fsti.cod_municipio = i.codigo_ibge " +
            "WHERE i.municipio ILIKE :municipio " +
            "ORDER BY fsti.referencia DESC " +
            "LIMIT 1",
            nativeQuery = true)
    Optional<Long> buscarQuantidadeFamiliasTrabalhoInfantilPorMunicipio(@Param("municipio") String municipio);
}

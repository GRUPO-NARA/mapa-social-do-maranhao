package mpma.mapa.repository.AssistencialSocial;

import mpma.mapa.entity.AssistencialSocial.PessoasInscritasNoCadastroUnicoPorSexoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository para o indicador de pessoas inscritas no Cadastro Único por sexo.
 *
 * Esta interface representa a persistência do indicador na camada de Assistência Social,
 * retornando apenas o registro mais recente por município.
 */
@Repository
public interface PessoasInscritasNoCadastroUnicoPorSexoRepository extends JpaRepository<PessoasInscritasNoCadastroUnicoPorSexoEntity, Long> {
    @Query(value = "SELECT ps.valor FROM assistencia_social.pessoas_inscritas_cadastro_unico_sexo ps " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON ps.cod_municipio = i.codigo_ibge " +
            "WHERE ps.referencia = :ano AND i.municipio ILIKE :municipio",
            nativeQuery = true)
    Long buscarPessoasInscritasNoCadastroUnicoPorSexo(@Param("ano") Integer ano, @Param("municipio") String municipio);
public interface PessoasInscritasNoCadastroUnicoPorSexoRepository extends JpaRepository<pessoasInscritasNoCadastroUnicoPorSexoEntity, Long> {
    /**
     * Busca o valor do indicador de pessoas inscritas no Cadastro Único por sexo para o município informado.
     *
     * A query nativa ordena por referência decrescente e limita o resultado a one para garantir a
     * recuperação da fotografia mais atual no banco de dados, ignorando registros anteriores.
     *
     * @param municipio nome do município usado na pesquisa
     * @return Optional<Long> valor do indicador no registro mais recente encontrado
     */
    @Query(value = "SELECT ps.valor " +
                   "FROM assistencia_social.pessoas_inscritas_no_cadastro_unico_por_sexo ps " +
                   "JOIN dados_estadual.referencias_codigos_municipais i ON ps.cod_municipio = i.codigo_ibge " +
                   "WHERE i.municipio = :municipio " +
                   "ORDER BY ps.referencia DESC " +   // troque 'referencia' pelo nome correto da coluna de data/ano se for outro
                   "LIMIT 1",
           nativeQuery = true)
    Optional<Long> buscarPessoasInscritasNoCadastroUnicoPorSexo(@Param("municipio") String municipio);
}
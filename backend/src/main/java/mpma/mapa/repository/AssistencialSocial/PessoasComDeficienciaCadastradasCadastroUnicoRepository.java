package mpma.mapa.repository.AssistencialSocial;

import mpma.mapa.entity.AssistencialSocial.PessoasComDeficienciaCadastradasCadastroUnicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository para o indicador de pessoas com deficiência cadastradas no Cadastro Único.
 *
 * A interface representa o acesso a dados históricos desse indicador na camada de Assistência Social,
 * garantindo a seleção do registro mais recente por município.
 */
@Repository
public interface PessoasComDeficienciaCadastradasCadastroUnicoRepository extends JpaRepository<PessoasComDeficienciaCadastradasCadastroUnicoEntity, Long> {

    /**
     * Busca o número de pessoas com deficiência cadastradas no Cadastro Único para o município informado.
     *
     * A query nativa aplica <code>ORDER BY pdcu.referencia DESC LIMIT 1</code> para ignorar registros
     * de anos anteriores e retornar apenas a fotografia mais recente disponível no banco.
     *
     * @param municipio nome do município usado na pesquisa
     * @return Long valor do indicador no registro mais recente encontrado
     */
    @Query(value = "SELECT pdcu.valor FROM assistencia_social.pessoas_com_deficiencia_cadastradas_cadastro_unico pdcu " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON pdcu.cod_municipio = i.codigo_ibge " +
            "WHERE i.municipio ILIKE :municipio " +
            "ORDER BY pdcu.referencia DESC " + // Ordena do ano mais recente para o mais antigo
            "LIMIT 1",                         // Garante que apenas o primeiro (mais recente) seja retornado
            nativeQuery = true)
    Long buscarPessoasComDeficienciaCadastradasCadastroUnicoPorMunicipio(@Param("municipio") String municipio);

}
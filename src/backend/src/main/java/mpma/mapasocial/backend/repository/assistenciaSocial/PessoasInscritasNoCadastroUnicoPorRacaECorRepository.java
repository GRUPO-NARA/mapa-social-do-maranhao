package mpma.mapasocial.backend.repository.assistenciaSocial;

import mpma.mapasocial.backend.entity.assistenciaSocial.pessoasInscritasNoCadastroUnicoPorRacaECorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository para o indicador de pessoas inscritas no Cadastro Único por raça e cor.
 *
 * Esta interface dá acesso à camada de dados do indicador de assistência social,
 * selecionando o registro mais recente por município para evitar informações defasadas.
 */
@Repository
public interface PessoasInscritasNoCadastroUnicoPorRacaECorRepository extends JpaRepository<pessoasInscritasNoCadastroUnicoPorRacaECorEntity, Long> {

    /**
     * Busca o valor do indicador de pessoas inscritas no Cadastro Único por raça e cor para o município informado.
     *
     * A pesquisa utiliza <code>ORDER BY prc.referencia DESC LIMIT 1</code> para garantir que, mesmo havendo
     * múltiplos anos de referência, seja retornada a fotografia mais recente disponível no banco.
     *
     * @param municipio nome do município usado na pesquisa
     * @return Long valor do indicador no registro mais recente encontrado
     */
    @Query(value = "SELECT prc.valor FROM assistencia_social.pessoas_inscritas_no_cadastro_unico_por_raca_e_cor prc " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON prc.cod_municipio = i.codigo_ibge " +
            "WHERE i.municipio ILIKE :municipio " +
            "ORDER BY prc.referencia DESC " + // Corrigido o espaçamento e adicionada a ordenação decrescente
            "LIMIT 1",                         // Limita para trazer apenas o ano mais recente
            nativeQuery = true)
    Long buscarPessoasInscritasNoCadastroUnicoPorRacaECor(@Param("municipio") String municipio);
}
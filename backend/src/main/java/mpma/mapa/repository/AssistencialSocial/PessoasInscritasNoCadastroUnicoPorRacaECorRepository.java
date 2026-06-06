package mpma.mapa.repository.AssistencialSocial;

import mpma.mapa.entity.AssistencialSocial.PessoasInscritasNoCadastroUnicoPorRacaECorEntity;
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
public interface PessoasInscritasNoCadastroUnicoPorRacaECorRepository extends JpaRepository<PessoasInscritasNoCadastroUnicoPorRacaECorEntity, Long> {
    @Query(value = "SELECT prc.valor FROM assistencia_social.pessoas_inscritas_no_cadastro_unico_por_raca_e_cor prc " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON prc.cod_municipio = i.codigo_ibge " +
            "WHERE i.municipio ILIKE :municipio " +
            "ORDER BY prc.referencia DESC " + // Corrigido o espaçamento e adicionada a ordenação decrescente
            "LIMIT 1",                         // Limita para trazer apenas o ano mais recente
            nativeQuery = true)
    Long buscarPessoasInscritasNoCadastroUnicoPorRacaECor(@Param("municipio") String municipio);
}
package mpma.mapasocial.backend.repository.assistenciaSocial;

import mpma.mapasocial.backend.entity.assistenciaSocial.pessoasInscritasNoCadastroUnicoPorRacaECorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoasInscritasNoCadastroUnicoPorRacaECorRepository extends JpaRepository<pessoasInscritasNoCadastroUnicoPorRacaECorEntity, Long> {

    @Query(value = "SELECT prc.valor FROM assistencia_social.pessoas_inscritas_no_cadastro_unico_por_raca_e_cor prc " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON prc.cod_municipio = i.codigo_ibge " +
            "WHERE i.municipio ILIKE :municipio " +
            "ORDER BY prc.referencia DESC " + // Corrigido o espaçamento e adicionada a ordenação decrescente
            "LIMIT 1",                         // Limita para trazer apenas o ano mais recente
            nativeQuery = true)
    Long buscarPessoasInscritasNoCadastroUnicoPorRacaECor(@Param("municipio") String municipio);
}
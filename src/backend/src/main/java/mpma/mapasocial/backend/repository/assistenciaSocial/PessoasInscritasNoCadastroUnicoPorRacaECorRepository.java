package mpma.mapasocial.backend.repository.assistenciaSocial;

import mpma.mapasocial.backend.entity.assistenciaSocial.pessoasInscritasNoCadastroUnicoPorRacaECorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoasInscritasNoCadastroUnicoPorRacaECorRepository extends JpaRepository<pessoasInscritasNoCadastroUnicoPorRacaECorEntity, Long> {
    @Query(value = "SELECT prc.valor FROM assistencia_social.pessoas_inscritas_cadastro_unico_raca_cor prc " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON prc.cod_municipio = i.codigo_ibge " +
            "WHERE prc.referencia = :ano AND i.municipio ILIKE :municipio", nativeQuery = true)
    Long buscarPessoasInscritasNoCadastroUnicoPorRacaECor(@Param("ano") Integer ano, @Param("municipio") String municipio);
}

package mpma.mapasocial.backend.repository.assistenciaSocial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mpma.mapasocial.backend.entity.assistenciaSocial.familiasEmSituacaoDeRuaInscritasCadastroUnicoEntity;

@Repository
public interface FamiliasEmSituacaoDeRuaInscritasCadastroUnicoRepository extends JpaRepository<familiasEmSituacaoDeRuaInscritasCadastroUnicoEntity, Long> {

    @Query(value = "SELECT f.valor FROM assistencia_social.familias_situacao_rua f " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON f.cod_municipio = i.codigo_ibge " +
            "WHERE f.referencia = :ano AND i.municipio ILIKE :municipio", nativeQuery = true)
    Long buscarQuantidadeFamiliasRuaPorMunicipio(@Param("ano") Integer ano, @Param("municipio") String municipio);
}
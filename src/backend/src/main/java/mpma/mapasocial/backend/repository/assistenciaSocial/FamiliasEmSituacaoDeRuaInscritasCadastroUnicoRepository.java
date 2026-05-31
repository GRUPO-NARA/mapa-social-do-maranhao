package mpma.mapasocial.backend.repository.assistenciaSocial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mpma.mapasocial.backend.entity.assistenciaSocial.familiasEmSituacaoDeRuaInscritasCadastroUnicoEntity;

import java.util.Optional;

@Repository
public interface FamiliasEmSituacaoDeRuaInscritasCadastroUnicoRepository extends JpaRepository<familiasEmSituacaoDeRuaInscritasCadastroUnicoEntity, Long> {

    @Query(value = "SELECT f.valor FROM assistencia_social.familias_em_situacao_de_rua_inscritas_cadastro_unico f " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON f.cod_municipio = i.codigo_ibge " +
            "WHERE i.municipio ILIKE :municipio " +
            "ORDER BY f.referencia DESC " +
            "LIMIT 1", nativeQuery = true)
    Optional<Long> buscarQuantidadeFamiliasRuaPorMunicipio(@Param("municipio") String municipio);
}

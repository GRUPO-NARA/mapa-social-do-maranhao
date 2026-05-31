package mpma.mapasocial.backend.repository.assistenciaSocial;

import mpma.mapasocial.backend.entity.assistenciaSocial.familiasEmSituacaoDeTrabalhoInfantilCadastroUnicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FamiliasEmSituacaoDeTrabalhoInfantilCadastroUnicoRepository extends JpaRepository<familiasEmSituacaoDeTrabalhoInfantilCadastroUnicoEntity, Long> {
    @Query(value = "SELECT fsti.valor FROM assistencia_social.familias_em_situacao_de_trabalho_infantil_cadastro_unico fsti " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON fsti.cod_municipio = i.codigo_ibge " +
            "WHERE i.municipio ILIKE :municipio " +
            "ORDER BY fsti.referencia DESC " +
            "LIMIT 1",
            nativeQuery = true)
    Optional<Long> buscarQuantidadeFamiliasTrabalhoInfantilPorMunicipio(@Param("municipio") String municipio);
}

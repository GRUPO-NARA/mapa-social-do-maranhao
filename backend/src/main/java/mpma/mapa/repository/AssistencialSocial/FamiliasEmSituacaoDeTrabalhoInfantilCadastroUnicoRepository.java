package mpma.mapa.repository.AssistencialSocial;

import mpma.mapa.entity.AssistencialSocial.FamiliasEmSituacaoDeTrabalhoInfantilCadastroUnicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FamiliasEmSituacaoDeTrabalhoInfantilCadastroUnicoRepository extends JpaRepository<FamiliasEmSituacaoDeTrabalhoInfantilCadastroUnicoEntity, Long> {
    @Query(value = "SELECT fsti.valor FROM assistencia_social.familias_situacao_trabalho_infantil fsti " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON fsti.cod_municipio = i.codigo_ibge " +
            "WHERE fsti.referencia = :ano AND i.municipio ILIKE :municipio",
            nativeQuery = true)
    Long buscarQuantidadeFamiliasTrabalhoInfantilPorMunicipio(@Param("ano") Integer ano,@Param("municipio") String municipio);
}

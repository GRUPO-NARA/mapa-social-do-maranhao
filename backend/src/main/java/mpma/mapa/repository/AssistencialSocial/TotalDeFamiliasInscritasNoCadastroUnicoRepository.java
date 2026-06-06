package mpma.mapa.repository.AssistencialSocial;

import mpma.mapa.entity.AssistencialSocial.TotalDeFamiliasInscritasNoCadastroUnicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalDeFamiliasInscritasNoCadastroUnicoRepository extends JpaRepository<TotalDeFamiliasInscritasNoCadastroUnicoEntity, Long> {
    @Query(value = "SELECT tf.valor FROM assistencia_social.total_familias_inscritas_cadastro_unico tf " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON tf.cod_municipio = i.codigo_ibge " +
            "WHERE tf.referencia = :ano AND i.municipio ILIKE :municipio",
            nativeQuery = true)
    Long buscarTotalDeFamiliasInscritasNoCadastroUnico(@Param("ano") Integer ano, @Param("municipio") String municipio);
}

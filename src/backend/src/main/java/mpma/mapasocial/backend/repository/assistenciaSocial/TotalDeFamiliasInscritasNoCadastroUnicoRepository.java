package mpma.mapasocial.backend.repository.assistenciaSocial;

import mpma.mapasocial.backend.entity.assistenciaSocial.totalDeFamiliasInscritasNoCadastroUnicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalDeFamiliasInscritasNoCadastroUnicoRepository extends JpaRepository<totalDeFamiliasInscritasNoCadastroUnicoEntity, Long> {

    @Query(value = "SELECT tf.valor FROM assistencia_social.total_de_familias_inscritas_no_cadastro_unico tf " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON tf.cod_municipio = i.codigo_ibge " +
            "WHERE i.municipio ILIKE :municipio " +
            "ORDER BY tf.referencia DESC " +
            "LIMIT 1",
            nativeQuery = true)
    Long buscarTotalDeFamiliasInscritasNoCadastroUnico(@Param("municipio") String municipio);
}
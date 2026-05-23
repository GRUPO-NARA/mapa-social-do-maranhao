package mpma.mapasocial.backend.repository.assistenciaSocial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mpma.mapasocial.backend.entity.assistenciaSocial.domiciliosComAguaEncanadaEntity;

@Repository
public interface DomiciliosComAguaEncanadaRepository extends JpaRepository<domiciliosComAguaEncanadaEntity, Long> {
    @Query(value = "SELECT dae.valor FROM assistencia_social.domicilios_com_agua_encanada dae " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON dae.cod_municipio = i.codigo_ibge " +
            "WHERE dae.referencia = :ano AND i.municipio ILIKE :municipio",
            nativeQuery = true)
    Long buscarDomiciliosComAguaEncanadaPorMunicipio(@Param("ano") Integer ano, @Param("municipio") String municipio);
}

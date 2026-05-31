package mpma.mapasocial.backend.repository.assistenciaSocial;

import mpma.mapasocial.backend.entity.assistenciaSocial.domiciliosComEnergiaEletricaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DomiciliosComEnergiaEletricaRepository extends JpaRepository<domiciliosComEnergiaEletricaEntity, Long> {

    @Query(value = "SELECT dee.valor " +
            "FROM assistencia_social.domicilios_com_energia_eletrica dee " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON dee.cod_municipio = i.codigo_ibge " +
            "WHERE i.municipio = :municipio " +
            "ORDER BY dee.referencia DESC " +
            "LIMIT 1",
            nativeQuery = true)
    Optional<Long> buscarDomiciliosComEnergiaPorMunicipio(@Param("municipio") String municipio);

}

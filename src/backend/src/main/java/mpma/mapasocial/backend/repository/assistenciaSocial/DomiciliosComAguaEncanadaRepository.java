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
            "WHERE i.municipio ILIKE :municipio " +
            "ORDER BY dae.referencia DESC " + // Ordena do ano de referência mais recente para o mais antigo
            "LIMIT 1",                         // Garante o retorno apenas da linha mais atualizada
            nativeQuery = true)
    Long buscarDomiciliosComAguaEncanadaPorMunicipio(@Param("municipio") String municipio);
}
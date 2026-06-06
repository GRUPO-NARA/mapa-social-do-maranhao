package mpma.mapa.repository.AssistencialSocial;

import mpma.mapa.entity.AssistencialSocial.ProgramaAuxilioGasParaBrasileiroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramaAuxilioGasParaBrasileiroRepository extends JpaRepository<ProgramaAuxilioGasParaBrasileiroEntity, Long> {
    @Query(value = "SELECT pag.valor FROM assistencia_social.programa_auxilio_gas_para_brasileiros pag " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON pag.cod_municipio = i.codigo_ibge " +
            "WHERE pag.referencia = :ano AND i.municipio ILIKE :municipio",
            nativeQuery = true)
    Long buscarAuxilioGasParaBrasileiroPorMunicipio(@Param("ano") Integer ano, @Param("municipio") String municipio);
}

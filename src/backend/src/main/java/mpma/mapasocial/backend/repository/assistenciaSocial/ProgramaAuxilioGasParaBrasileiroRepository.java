package mpma.mapasocial.backend.repository.assistenciaSocial;

import mpma.mapasocial.backend.entity.assistenciaSocial.programaAuxilioGasParaBrasileiroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramaAuxilioGasParaBrasileiroRepository extends JpaRepository<programaAuxilioGasParaBrasileiroEntity, Long> {

    @Query(value = "SELECT pag.valor FROM assistencia_social.programa_auxilio_gas_para_brasileiros pag " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON pag.cod_municipio = i.codigo_ibge " +
            "WHERE i.municipio ILIKE :municipio " +
            "ORDER BY pag.referencia DESC " + // Corrigido de f.referencia para pag.referencia
            "LIMIT 1",                        // Removidas as vírgulas e aspas extras da concatenação
            nativeQuery = true)
    Long buscarAuxilioGasParaBrasileiroPorMunicipio(@Param("municipio") String municipio);
}
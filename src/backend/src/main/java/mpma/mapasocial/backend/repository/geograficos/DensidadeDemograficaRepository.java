package mpma.mapasocial.backend.repository.geograficos;

import org.springframework.data.jpa.repository.JpaRepository;
import mpma.mapasocial.backend.entity.geograficos.densidadeDemograficaEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DensidadeDemograficaRepository extends JpaRepository<densidadeDemograficaEntity, Long> {
    @Query(value = "SELECT dd.valor FROM geograficos.densidade_demografica dd " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON i.codigo_ibge = dd.cod_municipio " +
            "WHERE i.municipio = :municipio", nativeQuery = true)
    Double buscarDensidadeDemograficaPorMunicipio(@Param("municipio") String municipio);
}

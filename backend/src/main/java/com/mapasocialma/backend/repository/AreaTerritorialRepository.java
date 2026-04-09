package com.mapasocialma.backend.repository;

import com.mapasocialma.backend.entity.AreaTerritorialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AreaTerritorialRepository extends JpaRepository<AreaTerritorialEntity, String> {
    @Query(value = """
        SELECT 
            dd.valor
        FROM 
            dados_geograficos.area_territorial as dd
        INNER JOIN 
            dados_gerais.informacoes dg ON TRIM(dd.cod_municipio) = TRIM(dg.codigo_ibge)
        WHERE  
            dg.nome_municipio ILIKE concat('%', :nomeMunicipio, '%');
    """, nativeQuery = true)
    List<Double> findAreaTerritorialByMunicipio(@Param("nomeMunicipio") String nomeMunicipio);
}

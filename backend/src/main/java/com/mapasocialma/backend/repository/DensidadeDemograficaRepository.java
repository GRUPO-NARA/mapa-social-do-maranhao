package com.mapasocialma.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mapasocialma.backend.entity.DensidadeDemograficaEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DensidadeDemograficaRepository extends JpaRepository<DensidadeDemograficaEntity, String> {

    @Query(value = """
        SELECT 
            dd.valor
        FROM 
            dados_geograficos.densidade_demografica as dd
        INNER JOIN 
            dados_gerais.informacoes dg ON TRIM(dd.cod_municipio) = TRIM(dg.codigo_ibge)
        WHERE  
            dg.nome_municipio ILIKE concat('%', :nomeMunicipio, '%');
    """, nativeQuery = true)
    List<Double> findDensidadeDemograficaByMunicipio(@Param("nomeMunicipio") String nomeMunicipio);
}
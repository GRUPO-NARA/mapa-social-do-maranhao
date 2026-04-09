package com.mapasocialma.backend.repository;

import com.mapasocialma.backend.entity.QuantidadeDeMulheresEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface QuantidadeDeMulheresRepository extends JpaRepository<QuantidadeDeMulheresEntity,String>{
    @Query(value = """
        SELECT 
            dd.valor
        FROM 
            dados_demograficos.quantidade_de_mulheres as dd
        INNER JOIN 
            dados_gerais.informacoes dg ON TRIM(dd.cod_municipio) = TRIM(dg.codigo_ibge)
        WHERE  
            dg.nome_municipio ILIKE concat('%', :nomeMunicipio, '%');
    """, nativeQuery = true)
    List<BigInteger>findQuantidadeDeMulheresByMunicipio (@Param("nomeMunicipio")String nomeMunicipio);
}

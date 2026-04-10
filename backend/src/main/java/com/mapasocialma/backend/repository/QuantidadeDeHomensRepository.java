package com.mapasocialma.backend.repository;

import com.mapasocialma.backend.entity.QuantidadeDeHomensEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface QuantidadeDeHomensRepository extends JpaRepository<QuantidadeDeHomensEntity, String> {
    @Query(value = """
        SELECT 
            dd.valor
        FROM 
            dados_demograficos.quantidade_de_homens as dd
        INNER JOIN 
            dados_gerais.informacoes dg ON TRIM(dd.cod_municipio) = TRIM(dg.codigo_ibge)
        WHERE  
            dg.nome_municipio ILIKE concat('%', :nomeMunicipio, '%');
    """, nativeQuery = true)
    List<BigInteger> findQuantidadeDeHomensByMunicipio(@Param("nomeMunicipio") String nomeMunicipio);

}

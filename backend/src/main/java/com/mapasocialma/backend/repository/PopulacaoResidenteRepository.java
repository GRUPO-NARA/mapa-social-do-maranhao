package com.mapasocialma.backend.repository;

import com.mapasocialma.backend.entity.PopulacaoResidenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface PopulacaoResidenteRepository extends JpaRepository<PopulacaoResidenteEntity, String> {
    @Query(
            value = """
            SELECT
                dd.valor as populacao_total
            FROM
                dados_demograficos.população_residente dd
            INNER JOIN
                dados_gerais.informacoes i ON TRIM(dd.cod_municipio) = TRIM(i.codigo_ibge)
            WHERE
                i.nome_municipio ILIKE concat('%', :nomeMunicipio ,'%');
            """, nativeQuery = true)
    List<BigInteger> findPopulacaoResidenteByMunicipio(@Param("nomeMunicipio") String nomeMunicipio);
    @Query( value = "SELECT SUM(dd.valor)FROM dados_demograficos.população_residente dd ", nativeQuery = true )
    Long findPopulacaoResidenteTotal();
}

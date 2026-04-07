package com.mapasocialma.backend.repository;

import com.mapasocialma.backend.entity.ProdutoInternoBrutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutoInternoBrutoRepository extends JpaRepository<ProdutoInternoBrutoEntity, String> {
    @Query(value = """
        SELECT
            de.valor as produto_interno_bruto
        FROM
            dados_economicos.produto_interno_bruto de
        INNER JOIN
            dados_gerais.informacoes i ON TRIM(de.cod_municipio) = TRIM(i.codigo_ibge)
        WHERE
            i.nome_municipio ILIKE concat('%', :nomeMunicipio ,'%');
        
        """, nativeQuery = true)
    List<Double> findAllProdutoInternoBrutoByMunicipio(@Param("nomeMunicipio") String nomeMunicipio);
}

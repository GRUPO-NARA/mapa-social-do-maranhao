package mpma.mapasocial.backend.repository;

import mpma.mapasocial.backend.dto.DadosPrincipaisSidraDTO;
import mpma.mapasocial.backend.entity.demograficos.populacaoResidenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InformacoesRepository extends JpaRepository<populacaoResidenteEntity, Long> {
    @Query(value = "SELECT json_build_array(\n" +
            "               json_build_object('indicador', 'população residente', 'valor', pr.valor, 'data_coleta', pr.referencia),\n" +
            "               json_build_object('indicador', 'quantidade de homens', 'valor', qh.valor, 'data_coleta', qh.referencia),\n" +
            "               json_build_object('indicador', 'quantidade de mulheres', 'valor', qm.valor, 'data_coleta', qm.referencia),\n" +
            "               json_build_object('indicador', 'densidade demográfica', 'valor', dd.valor, 'data_coleta', dd.referencia),\n" +
            "               json_build_object('indicador', 'área territorial', 'valor', at.valor, 'data_coleta', at.referencia)\n" +
            "       ) AS dados_principais_SIDRA\n" +
            "FROM dados_estadual.referencias_codigos_municipais i\n" +
            "         JOIN demograficos.populacao_residente pr ON i.codigo_ibge = pr.cod_municipio\n" +
            "         JOIN demograficos.quantidade_de_homens qh ON i.codigo_ibge = qh.cod_municipio\n" +
            "         JOIN demograficos.quantidade_de_mulheres qm ON i.codigo_ibge = qm.cod_municipio\n" +
            "         JOIN geograficos.densidade_demografica dd ON i.codigo_ibge = dd.cod_municipio\n" +
            "         JOIN geograficos.area_territorial at ON i.codigo_ibge = at.cod_municipio\n" +
            "WHERE i.municipio = :municipio \n" +
            "GROUP BY\n" +
            "    pr.referencia, pr.valor,\n" +
            "    qh.referencia, qh.valor,\n" +
            "    qm.referencia, qm.valor,\n" +
            "    dd.referencia, dd.valor,\n" +
            "    at.referencia, at.valor\n" +
            "ORDER BY\n" +
            "    pr.referencia DESC,\n" +
            "    qh.referencia DESC,\n" +
            "    qm.referencia DESC,\n" +
            "    dd.referencia DESC,\n" +
            "    at.referencia DESC\n" +
            "LIMIT 1;\n" +
            "\n", nativeQuery = true)
    String buscarDadosPrincipaisSIDRA(@Param("municipio") String municipio);
}

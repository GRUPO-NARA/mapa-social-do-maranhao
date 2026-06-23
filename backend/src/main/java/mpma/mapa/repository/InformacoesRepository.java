package mpma.mapa.repository;

import mpma.mapa.entity.Demograficos.PopulacaoResidenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InformacoesRepository extends JpaRepository<PopulacaoResidenteEntity, Long> {
    @Query(value = """
            SELECT json_build_array(
                           json_build_object('Quantidade de Pessoas',pr.valor, 'Referência dos dados', pr.referencia, 'Fonte dos Dados', pr.fonte),
                           json_build_object('Quantidade de Homens', qh.valor, 'Referência dos dados', qh.referencia, 'Fonte dos Dados', qh.fonte),
                           json_build_object('Quantidade de Mulheres', qm.valor, 'Referência dos dados', qm.referencia, 'Fonte dos Dados', qm.fonte),
                           json_build_object('Densidade Demográfica', dd.valor, 'Referência dos dados', dd.referencia, 'Fonte dos Dados', dd.fonte),
                           json_build_object('Área Territorial', at.valor, 'Referência dos dados', at.referencia, 'Fonte dos Dados', at.fonte)
                   ) AS dados_principais_municipal
            FROM dados_estadual.referencias_codigos_municipais i
                     JOIN demograficos.populacao_residente pr ON i.codigo_ibge = pr.cod_municipio
                     JOIN demograficos.quantidade_de_homens qh ON i.codigo_ibge = qh.cod_municipio
                     JOIN demograficos.quantidade_de_mulheres qm ON i.codigo_ibge = qm.cod_municipio
                     JOIN geograficos.densidade_demografica dd ON i.codigo_ibge = dd.cod_municipio
                     JOIN geograficos.area_territorial at ON i.codigo_ibge = at.cod_municipio
            WHERE i.municipio = ?1
            GROUP BY
                pr.referencia, pr.valor, pr.referencia,
                qh.referencia, qh.valor, qh.referencia,
                qm.referencia, qm.valor, qm.referencia,
                dd.referencia, dd.valor, dd.referencia,
                at.referencia, at.valor, at.referencia, pr.fonte, qh.fonte, qm.fonte, dd.fonte, at.fonte
            ORDER BY
                pr.referencia DESC,
                qh.referencia DESC,
                qm.referencia DESC,
                dd.referencia DESC,
                at.referencia DESC
            LIMIT 1;
            
            """, nativeQuery = true)
    String buscarDadosPrincipaisDoMunicipio(String municipio);

    @Query(value = """
        SELECT table_name
        FROM information_schema.tables
        WHERE table_schema = ?1
        AND table_type = 'BASE TABLE';
    """, nativeQuery = true)
    List<String> buscarTabelasDoSchema(@Param("schema") String schema);
}

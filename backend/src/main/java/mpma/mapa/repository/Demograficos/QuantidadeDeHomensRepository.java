package mpma.mapa.repository.Demograficos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mpma.mapa.entity.Demograficos.QuantidadeDeHomensEntity;
@Repository
public interface QuantidadeDeHomensRepository extends JpaRepository<QuantidadeDeHomensEntity, Long> {
    @Query(
            value = """
            SELECT json_build_object(
                               'Quantidade de Homens', t.valor,
                               'Referência dos Dados', t.referencia,
                               'Fonte dos Dados', t.fonte
                        )
                        FROM demograficos.quantidade_de_homens as t
                        JOIN dados_estadual.referencias_codigos_municipais rcm on t.cod_municipio = rcm.codigo_ibge
                        WHERE rcm.municipio = ?1
                        GROUP BY t.valor, t.referencia, t.fonte
                        ORDER BY t.referencia DESC
                        LIMIT 1;
            """,
            nativeQuery = true
    )
    String buscarQuantidadeDeHomensDoMunicipio(String municipio);
}

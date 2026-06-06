package mpma.mapa.repository.Demograficos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mpma.mapa.entity.Demograficos.QuantidadeDeMulheresEntity;

@Repository
public interface QuantidadeDeMulheresRepository extends JpaRepository<QuantidadeDeMulheresEntity, Long> {
    @Query(
            value = """
            SELECT json_build_object(
                               'Quantidade de Mulheres', t.valor,
                               'Referência dos Dados', t.referencia,
                               'Fonte dos Dados', t.fonte
                        )
                        FROM demograficos.quantidade_de_mulheres as t
                        JOIN dados_estadual.referencias_codigos_municipais rcm on t.cod_municipio = rcm.codigo_ibge
                        WHERE rcm.municipio = ?1
                        GROUP BY t.valor, t.referencia, t.fonte
                        ORDER BY t.referencia DESC
                        LIMIT 1;
            """,
            nativeQuery = true
    )
    String buscarQuantidadeDeMulheresDoMunicipio(String municipio);
}

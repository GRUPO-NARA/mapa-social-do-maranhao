package mpma.mapa.repository.Geograficos;

import org.springframework.data.jpa.repository.JpaRepository;
import mpma.mapa.entity.Geograficos.DensidadeDemograficaEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DensidadeDemograficaRepository extends JpaRepository<DensidadeDemograficaEntity, Long> {
    @Query(
            value = """
            SELECT json_build_object(
                               'Densidade Demográfica', t.valor,
                               'Referência dos Dados', t.referencia,
                               'Fonte dos Dados', t.fonte
                        )
                        FROM geograficos.densidade_demografica as t
                        JOIN dados_estadual.referencias_codigos_municipais rcm on t.cod_municipio = rcm.codigo_ibge
                        WHERE rcm.municipio = ?1
                        GROUP BY t.valor, t.referencia, t.fonte
                        ORDER BY t.referencia DESC
                        LIMIT 1;
            """
            ,nativeQuery = true
    )
    String buscarDensidadeDemograficaDoMunicipio(String municipio);
}

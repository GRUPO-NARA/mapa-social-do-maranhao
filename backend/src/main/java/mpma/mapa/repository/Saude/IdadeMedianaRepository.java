package mpma.mapa.repository.Saude;

import mpma.mapa.entity.Saude.IdadeMedianaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IdadeMedianaRepository extends JpaRepository<IdadeMedianaEntity, Long> {
    @Query(
            value = """
            SELECT json_build_object(
                               'Idade Mediana', t.valor,
                               'Referência dos Dados', t.referencia,
                               'Fonte dos Dados', t.fonte
                        )
                        FROM saude.idade_mediana as t
                        JOIN dados_estadual.referencias_codigos_municipais rcm on t.cod_municipio = rcm.codigo_ibge
                        WHERE rcm.municipio = ?1
                        GROUP BY t.valor, t.referencia, t.fonte
                        ORDER BY t.referencia DESC
                        LIMIT 1;
            """,
            nativeQuery = true
    )
    String buscarIdadeMedianaDoMunicipio(String municipio);
}

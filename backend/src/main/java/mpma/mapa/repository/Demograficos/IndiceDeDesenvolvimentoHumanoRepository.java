package mpma.mapa.repository.Demograficos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mpma.mapa.entity.Demograficos.IndiceDeDesenvolvimentoHumanoEntity;

import java.util.List;

@Repository
public interface IndiceDeDesenvolvimentoHumanoRepository extends JpaRepository<IndiceDeDesenvolvimentoHumanoEntity, Long> {
    @Query(
            value = """
            SELECT json_build_object(
                               'Índice de Desenvolvimento Humano', t.valor,
                               'Referência dos Dados', t.referencia,
                               'Fonte dos Dados', t.fonte
                        )
                        FROM demograficos.indice_de_desenvolvimento_humano as t
                        JOIN dados_estadual.referencias_codigos_municipais rcm on t.cod_municipio = rcm.codigo_ibge
                        WHERE rcm.municipio = ?1
                        GROUP BY t.valor, t.referencia, t.fonte
                        ORDER BY t.referencia DESC
                        LIMIT 1;
            """
            ,nativeQuery = true
    )
    String buscarIndiceDeDesenvolvimentoHumanoDoMunicipio(String municipio);

    @Query(value = "SELECT\n" +
            "    json_build_object(\n" +
            "        'Referência', idh.referencia,\n" +
            "        'Valor do IDH', idh.valor\n" +
            "    )\n" +
            "FROM demograficos.indice_de_desenvolvimento_humano as idh\n" +
            "JOIN dados_estadual.referencias_codigos_municipais as i ON i.codigo_ibge = idh.cod_municipio\n" +
            "WHERE i.municipio = :municipio", nativeQuery = true)
    List<String> buscarEvolucaoIndiceDeDesenvolvimentoHumanoDoMunicipio(@Param("municipio") String municipio);
}

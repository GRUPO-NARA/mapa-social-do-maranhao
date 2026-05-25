package mpma.mapasocial.backend.repository.demograficos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mpma.mapasocial.backend.entity.demograficos.indiceDeDesenvolvimentoHumanoEntity;

import java.util.List;

@Repository
public interface IndiceDeDesenvolvimentoHumanoRepository extends JpaRepository<indiceDeDesenvolvimentoHumanoEntity, Long> {
    @Query(value = "SELECT json_build_object(\n" +
            "               'IDH_municipal', idh.valor,\n" +
            "               'referencia', idh.referencia\n" +
            "       ) AS resultado\n" +
            "\n" +
            "FROM demograficos.indice_de_desenvolvimento_humano idh\n" +
            "JOIN dados_estadual.referencias_codigos_municipais i ON i.codigo_ibge = idh.cod_municipio\n" +
            "WHERE i.municipio = :municipio \n" +
            "GROUP BY idh.valor, idh.referencia\n" +
            "ORDER BY idh.referencia DESC\n" +
            "LIMIT 1;", nativeQuery = true)
    String buscarIndiceDeDesenvolvimentoHumanoPorMunicipio(@Param("municipio") String municipio);

    @Query(value = "SELECT\n" +
            "    json_build_object(\n" +
            "        'referencia', idh.referencia,\n" +
            "        'IDH', idh.valor\n" +
            "    )\n" +
            "FROM demograficos.indice_de_desenvolvimento_humano as idh\n" +
            "JOIN dados_estadual.referencias_codigos_municipais as i ON i.codigo_ibge = idh.cod_municipio\n" +
            "WHERE i.municipio = :municipio", nativeQuery = true)
    List<String> buscarEvolucaoIndiceDeDesenvolvimentoHumanoPorMunicipio(@Param("municipio") String municipio);
}

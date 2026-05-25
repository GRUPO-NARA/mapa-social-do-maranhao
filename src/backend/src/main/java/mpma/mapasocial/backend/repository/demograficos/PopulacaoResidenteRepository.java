package mpma.mapasocial.backend.repository.demograficos;

import org.springframework.data.jpa.repository.JpaRepository;
import mpma.mapasocial.backend.entity.demograficos.populacaoResidenteEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PopulacaoResidenteRepository extends JpaRepository<populacaoResidenteEntity, Long> {

    @Query(value = "SELECT pr.valor FROM demograficos.populacao_residente pr " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON pr.cod_municipio = i.codigo_ibge " +
            "WHERE i.municipio ILIKE :municipio ",
            nativeQuery = true)
    Long buscarPopulacaoResidente(@Param("municipio") String municipio);

    @Query(value = "SELECT json_build_object(\n" +
            "       'referencia', pr.referencia,\n" +
            "       'populacao_estado', SUM(pr.valor)\n" +
            "       )\n" +
            "FROM demograficos.populacao_residente as pr\n" +
            "GROUP BY pr.referencia\n" +
            "ORDER BY pr.referencia DESC\n" +
            "LIMIT 1;\n" +
            "\n", nativeQuery = true)
    String buscaPopulacaoResidenteEstadualDosDadosMaisRecentes();
}

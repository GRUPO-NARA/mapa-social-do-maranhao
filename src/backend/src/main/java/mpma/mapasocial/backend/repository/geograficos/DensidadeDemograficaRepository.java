package mpma.mapasocial.backend.repository.geograficos;

import org.springframework.data.jpa.repository.JpaRepository;
import mpma.mapasocial.backend.entity.geograficos.densidadeDemograficaEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository para o indicador de densidade demográfica na camada Geográficos.
 *
 * Esta interface representa a persistência dos dados de densidade demográfica
 * e expõe a consulta que recupera o valor do indicador a partir do municipio.
 */
public interface DensidadeDemograficaRepository extends JpaRepository<densidadeDemograficaEntity, Long> {

    /**
     * Busca a densidade demográfica do município informado.
     *
     * A query nativa seleciona o valor de densidade demográfica na tabela
     * <code>geograficos.densidade_demografica</code> e realiza junção com
     * <code>dados_estadual.referencias_codigos_municipais</code> para filtrar
     * pelo município via código IBGE.
     *
     * @param municipio nome do município usado no filtro da busca
     * @return Double valor da densidade demográfica do município
     */
    @Query(value = "SELECT dd.valor FROM geograficos.densidade_demografica dd " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON i.codigo_ibge = dd.cod_municipio " +
            "WHERE i.municipio = :municipio", nativeQuery = true)
    Double buscarDensidadeDemograficaPorMunicipio(@Param("municipio") String municipio);
}

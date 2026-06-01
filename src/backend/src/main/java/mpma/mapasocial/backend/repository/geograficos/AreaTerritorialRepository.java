package mpma.mapasocial.backend.repository.geograficos;
import mpma.mapasocial.backend.entity.geograficos.AreaTerritorialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository para o indicador de área territorial municipal na camada Geográficos.
 *
 * Esta interface expõe a consulta que recupera o valor da área territorial de um município
 * a partir da tabela de geograficos e faz a junção com a tabela de referências de códigos municipais.
 */
@Repository
public interface AreaTerritorialRepository extends JpaRepository<AreaTerritorialEntity, Long> {

    /**
     * Busca a área territorial do município informado.
     *
     * A query nativa seleciona o valor da área territorial na tabela
     * <code>geograficos.area_territorial</code> e utiliza a junção com
     * <code>dados_estadual.referencias_codigos_municipais</code> para filtrar
     * pelo nome do município via o código IBGE.
     *
     * @param municipio nome do município usado no filtro da busca
     * @return Double valor da área territorial do município
     */
    @Query(
            value = "SELECT at.valor FROM geograficos.area_territorial at " +
                    "JOIN dados_estadual.referencias_codigos_municipais i ON i.codigo_ibge = at.cod_municipio " +
                    "WHERE i.municipio = :municipio;"
    , nativeQuery = true)
    Double buscarAreaTerritoralPorMunicipio(@Param("municipio") String municipio);
}

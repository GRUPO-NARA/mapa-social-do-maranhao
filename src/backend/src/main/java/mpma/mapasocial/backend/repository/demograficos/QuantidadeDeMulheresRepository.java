package mpma.mapasocial.backend.repository.demograficos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mpma.mapasocial.backend.entity.demograficos.quantidadeDeMulheresEntity;

/**
 * Repository para o indicador demográfico de quantidade de mulheres por município.
 *
 * Esta interface expõe a consulta de valores populacionais femininos por município,
 * utilizando a tabela de códigos municipais para filtrar pelo nome do município.
 */
@Repository
public interface QuantidadeDeMulheresRepository extends JpaRepository<quantidadeDeMulheresEntity, Long> {

    /**
     * Busca a quantidade de mulheres para o município informado.
     *
     * @param municipio filtro de busca por texto para o nome do município
     * @return Long valor da quantidade de mulheres no município
     */
    @Query(value = "SELECT qm.valor FROM demograficos.quantidade_de_mulheres qm " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON i.codigo_ibge = qm.cod_municipio " +
            "WHERE i.municipio ILIKE :municipio", nativeQuery = true)
    Long buscarQuantidadeDeMulheresPorMunicipio(@Param("municipio") String municipio);
}

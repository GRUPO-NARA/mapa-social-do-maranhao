package mpma.mapasocial.backend.repository.demograficos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mpma.mapasocial.backend.entity.demograficos.quantidadeDeHomensEntity;
/**
 * Repository para o indicador demográfico de quantidade de homens por município.
 *
 * Esta interface mapeia a busca do valor populacional masculino no contexto demográfico,
 * com foco em consulta por município utilizando a tabela de referências de códigos municipais.
 */
@Repository
public interface QuantidadeDeHomensRepository extends JpaRepository<quantidadeDeHomensEntity, Long> {

    /**
     * Busca a quantidade de homens para o município informado.
     *
     * @param municipio filtro de busca por texto para o nome do município
     * @return Long valor da quantidade de homens no município
     */
    @Query(value = "SELECT qh.valor FROM demograficos.quantidade_de_homens qh " +
    "JOIN dados_estadual.referencias_codigos_municipais i ON qh.cod_municipio = i.codigo_ibge " +
    "WHERE i.municipio ILIKE :municipio ", nativeQuery = true)
    Long buscarQuantidadeDeHomensPorMunicipio(@Param("municipio") String municipio);
}

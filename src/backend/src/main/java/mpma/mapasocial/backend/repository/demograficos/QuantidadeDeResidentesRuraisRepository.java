package mpma.mapasocial.backend.repository.demograficos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mpma.mapasocial.backend.entity.demograficos.quantidadeDeResidentesRuraisEntity;

/**
 * Repository para o indicador demográfico de quantidade de residentes rurais por município.
 *
 * Esta interface mantém o acesso aos dados de residentes rurais e utiliza a tabela de
 * referências de códigos de municípios para aplicar o filtro pelo nome do município.
 */
@Repository
public interface QuantidadeDeResidentesRuraisRepository extends JpaRepository<quantidadeDeResidentesRuraisEntity, Long> {

    /**
     * Busca a quantidade de residentes rurais para o município informado.
     *
     * @param municipio filtro de busca por texto para o nome do município
     * @return Long valor da quantidade de residentes rurais no município
     */
    @Query(value = "SELECT qrr.valor FROM demograficos.quantidade_de_residentes_rurais qrr " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON i.codigo_ibge = qrr.cod_municipio " +
            "WHERE i.municipio = :municipio;", nativeQuery = true)
    Long buscarQuantidadeDeResidentesRuraisPorMunicipio(@Param("municipio") String municipio);
}

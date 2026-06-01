package mpma.mapasocial.backend.repository.assistenciaSocial;

import mpma.mapasocial.backend.entity.assistenciaSocial.domiciliosComEnergiaEletricaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository para o indicador de domicílios com energia elétrica na Assistência Social.
 *
 * Esta interface encapsula a consulta de dados históricos por município e garante
 * que seja retornado apenas o valor mais recente disponível no banco.
 */
@Repository
public interface DomiciliosComEnergiaEletricaRepository extends JpaRepository<domiciliosComEnergiaEletricaEntity, Long> {

    /**
     * Busca o valor de domicílios com energia elétrica para o município informado.
     *
     * A query nativa faz a junção com a tabela de referências de códigos municipais
     * para permitir a pesquisa por nome do município. O uso de
     * <code>ORDER BY dee.referencia DESC LIMIT 1</code> serve para ignorar duplicidades
     * de múltiplos anos e retornar sempre a fotografia mais recente disponível no banco,
     * independentemente da fonte original (Ipeadata, SIDRA ou IBGE).
     *
     * @param municipio nome do município usado na pesquisa
     * @return Optional<Long> valor do indicador no registro mais recente encontrado
     */
    @Query(value = "SELECT dee.valor " +
            "FROM assistencia_social.domicilios_com_energia_eletrica dee " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON dee.cod_municipio = i.codigo_ibge " +
            "WHERE i.municipio = :municipio " +
            "ORDER BY dee.referencia DESC " +
            "LIMIT 1",
            nativeQuery = true)
    Optional<Long> buscarDomiciliosComEnergiaPorMunicipio(@Param("municipio") String municipio);

}

package mpma.mapasocial.backend.repository.assistenciaSocial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mpma.mapasocial.backend.entity.assistenciaSocial.domiciliosComAguaEncanadaEntity;

/**
 * Repository para o indicador de domicílios com água encanada na Assistência Social.
 *
 * Esta interface mapeia a busca de dados históricos por município e expõe a
 * consulta que retorna sempre o registro mais recente disponível na base.
 */
@Repository
public interface DomiciliosComAguaEncanadaRepository extends JpaRepository<domiciliosComAguaEncanadaEntity, Long> {

    /**
     * Busca o valor de domicílios com água encanada para o município informado.
     *
     * A query nativa realiza a junção com a tabela de referências de códigos municipais
     * para permitir comparação por nome do município. O uso de
     * <code>ORDER BY dae.referencia DESC LIMIT 1</code> é essencial para ignorar
     * duplicidades de múltiplos anos e retornar sempre a fotografia mais recente
     * disponível no banco de dados, seja ela proveniente do Ipeadata, SIDRA ou IBGE.
     *
     * @param municipio nome do município usado na pesquisa (consulta ILIKE)
     * @return Long valor do indicador no registro mais recente encontrado
     */
    @Query(value = "SELECT dae.valor FROM assistencia_social.domicilios_com_agua_encanada dae " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON dae.cod_municipio = i.codigo_ibge " +
            "WHERE i.municipio ILIKE :municipio " +
            "ORDER BY dae.referencia DESC " + // Ordena do ano de referência mais recente para o mais antigo
            "LIMIT 1",                         // Garante o retorno apenas da linha mais atualizada
            nativeQuery = true)
    Long buscarDomiciliosComAguaEncanadaPorMunicipio(@Param("municipio") String municipio);
}
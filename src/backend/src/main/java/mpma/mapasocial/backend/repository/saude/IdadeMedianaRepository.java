package mpma.mapasocial.backend.repository.saude;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository para o indicador de idade mediana na camada Saúde.
 *
 * Esta interface expõe a consulta que recupera a idade mediana de um município
 * para um ano de referência específico, fazendo a junção com a tabela de referências
 * de códigos municipais para filtrar pelo nome do município.
 */
public interface IdadeMedianaRepository extends JpaRepository<mpma.mapasocial.backend.entity.saude.idadeMedianaEntity, Long> {

    /**
     * Busca a idade mediana para o município e ano informados.
     *
     * A query nativa seleciona o valor de <code>idade_mediana.valor</code> da tabela
     * <code>saude.idade_mediana</code> e filtra pelo ano de referência e pelo município.
     * A junção com <code>dados_estadual.referencias_codigos_municipais</code> garante
     * que a pesquisa use o código IBGE correspondente ao município informado.
     *
     * @param ano ano de referência usado na busca
     * @param municipio nome do município usado no filtro da consulta
     * @return Double valor da idade mediana para o município e ano especificados
     */
    @Query(value = "SELECT idade_mediana.valor FROM saude.idade_mediana " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON idade_mediana.cod_municipio = i.codigo_ibge " +
            "WHERE idade_mediana.referencia = :ano AND i.municipio ILIKE :municipio;", nativeQuery = true)
    Double buscarIdadeMedianaPorMunicipio(@Param("ano") String ano, @Param("municipio") String municipio);
}

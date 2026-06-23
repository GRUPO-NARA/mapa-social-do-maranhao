package mpma.mapa.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import mpma.mapa.dto.ClusterizacaoDTO;
import mpma.mapa.repository.InformacoesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Validated
public class InformacoesService {

    @Autowired
    private InformacoesRepository informacoesRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final List<String> SCHEMAS_PERMITIDOS = List.of(
            "saude", "assistencia_social", "educacao"
    );

    public String principaisInformacoesMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio não pode ser em branco")
            String municipio) {
        return informacoesRepository.buscarDadosPrincipaisDoMunicipio(municipio);
    }

    public List<String> tabelasDoSchema(String schema) {
        return informacoesRepository.buscarTabelasDoSchema(schema);
    }

    public List<String> buscarEvolucaoDoIndicador(String schema, String indicador, String municipio) {

        if (!SCHEMAS_PERMITIDOS.contains(schema)) {
            throw new IllegalArgumentException("Schema inválido: " + schema);
        }

        if (!indicador.matches("[a-zA-Z0-9_]+")) {
            throw new IllegalArgumentException("Indicador inválido: " + indicador);
        }

        String sql = String.format(
                "SELECT json_build_object('Referência', x.referencia, 'Valor', x.valor) " +
                        "FROM %s.%s x " +
                        "JOIN dados_estadual.referencias_codigos_municipais i ON i.codigo_ibge = x.cod_municipio " +
                        "WHERE i.municipio = ?",
                schema, indicador
        );

        return jdbcTemplate.queryForList(sql, String.class, municipio);
    }

    public List<String> buscarResumoDoIndicador(String schema, String indicador, String municipio) {

        if (!SCHEMAS_PERMITIDOS.contains(schema)) {
            throw new IllegalArgumentException("Schema inválido: " + schema);
        }

        if (!indicador.matches("[a-zA-Z0-9_]+")) {
            throw new IllegalArgumentException("Indicador inválido: " + indicador);
        }

        String sql = String.format(
                "WITH dados AS (" +
                        "    SELECT " +
                        "        x.valor, " +
                        "        x.referencia, " +
                        "        FIRST_VALUE(x.valor) OVER (ORDER BY x.referencia DESC) as valor_recente, " +
                        "        FIRST_VALUE(x.referencia) OVER (ORDER BY x.referencia DESC) as ref_recente, " +
                        "        FIRST_VALUE(x.referencia) OVER (ORDER BY x.valor DESC, x.referencia DESC) as melhor_ano, " +
                        "        MAX(x.valor) OVER () as maior_valor, " +
                        "        FIRST_VALUE(x.referencia) OVER (ORDER BY x.valor ASC, x.referencia DESC) as pior_ano, " +
                        "        MIN(x.valor) OVER () as menor_valor, " +
                        "        FIRST_VALUE(x.valor) OVER (ORDER BY x.referencia ASC) as primeiro_valor, " +
                        "        MIN(x.referencia) OVER () as primeiro_ano " +
                        "    FROM dados_estadual.referencias_codigos_municipais i " +
                        "    JOIN %s.%s x ON i.codigo_ibge = x.cod_municipio " +
                        "    WHERE i.municipio = ? " +
                        ") " +
                        "SELECT json_build_object(" +
                        "    'VALOR MAIS RECENTE', d.valor_recente," +
                        "    'Referência', d.ref_recente," +
                        "    'MELHOR RESULTADO', d.melhor_ano," +
                        "    'Maior valor', d.maior_valor," +
                        "    'PONTO DE ATENÇÃO', d.pior_ano," +
                        "    'Menor valor', d.menor_valor," +
                        "    'VARIAÇÃO NO PERÍODO', CONCAT(CASE WHEN (d.valor_recente - d.primeiro_valor) >= 0 THEN '+' ELSE '' END, (d.valor_recente - d.primeiro_valor), ' p.p.')," +
                        "    'Evolução', CONCAT('de ', d.primeiro_ano, ' a ', d.ref_recente)" +
                        ")::text FROM dados d LIMIT 1",
                schema, indicador
        );

        return jdbcTemplate.queryForList(sql, String.class, municipio);
    }

    public List<String> buscarComparacaoTerritorial(String schema, String indicador, List<String> municipios) {

        if (!SCHEMAS_PERMITIDOS.contains(schema)) {
            throw new IllegalArgumentException("Schema inválido: " + schema);
        }

        if (!indicador.matches("[a-zA-Z0-9_]+")) {
            throw new IllegalArgumentException("Indicador inválido: " + indicador);
        }

        String sql = String.format(
                "WITH dados_filtrados AS (" +
                        "    SELECT " +
                        "        i.municipio," +
                        "        x.valor," +
                        "        x.referencia," +
                        "        MAX(x.referencia) OVER() as ultima_ref" +
                        "    FROM %s.%s x " +
                        "    JOIN dados_estadual.referencias_codigos_municipais i ON i.codigo_ibge = x.cod_municipio " +
                        "    WHERE i.municipio IN (:municipios)" +
                        ") " +
                        "SELECT json_build_object(" +
                        "    'Município', df.municipio," +
                        "    'Valor', df.valor," +
                        "    'Referência', df.referencia" +
                        ")::text " +
                        "FROM dados_filtrados df " +
                        "WHERE df.referencia = df.ultima_ref " +
                        "ORDER BY df.valor DESC",
                schema, indicador
        );

        Map<String, Object> params = new HashMap<>();
        params.put("municipios", municipios);

        return namedParameterJdbcTemplate.queryForList(sql, params, String.class);
    }

    public List<ClusterizacaoDTO.PontoMunicipal> buscarDadosParaClusterizacao(
            String schema,
            String indicador
    ) {
        if (!SCHEMAS_PERMITIDOS.contains(schema)) {
            throw new IllegalArgumentException("Schema inválido: " + schema);
        }

        if (!indicador.matches("[a-zA-Z0-9_]+")) {
            throw new IllegalArgumentException("Indicador inválido: " + indicador);
        }

        String sql = String.format(
                "SELECT i.municipio, AVG(x.valor)::double precision AS valor " +
                        "FROM %s.%s x " +
                        "JOIN dados_estadual.referencias_codigos_municipais i " +
                        "ON i.codigo_ibge = x.cod_municipio " +
                        "WHERE x.referencia = (" +
                        "    SELECT MAX(referencia) FROM %s.%s WHERE valor IS NOT NULL" +
                        ") " +
                        "AND x.valor IS NOT NULL " +
                        "GROUP BY i.municipio " +
                        "ORDER BY i.municipio",
                schema, indicador, schema, indicador
        );

        return jdbcTemplate.query(
                sql,
                (resultado, linha) -> new ClusterizacaoDTO.PontoMunicipal(
                        resultado.getString("municipio"),
                        resultado.getDouble("valor")
                )
        );
    }

    public Integer buscarReferenciaMaisRecente(String schema, String indicador) {
        if (!SCHEMAS_PERMITIDOS.contains(schema)) {
            throw new IllegalArgumentException("Schema inválido: " + schema);
        }

        if (!indicador.matches("[a-zA-Z0-9_]+")) {
            throw new IllegalArgumentException("Indicador inválido: " + indicador);
        }

        String sql = String.format(
                "SELECT MAX(referencia) FROM %s.%s WHERE valor IS NOT NULL",
                schema,
                indicador
        );

        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

}

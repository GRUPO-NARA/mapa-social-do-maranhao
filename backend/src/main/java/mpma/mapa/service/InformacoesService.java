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

    private static final String EXPRESSAO_ORDEM_REFERENCIA =
            "CASE " +
                    "WHEN %s.referencia::text ~ '^[0-9]{4}(-[0-9]{1,2})?$' THEN " +
                    "split_part(%s.referencia::text, '-', 1)::int * 100 + " +
                    "COALESCE(NULLIF(split_part(%s.referencia::text, '-', 2), '')::int, 12) " +
                    "ELSE 0 END";

    @Autowired
    private InformacoesRepository informacoesRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final List<String> SCHEMAS_DINAMICOS_PERMITIDOS = List.of(
            "saude", "assistencia_social", "educacao"
    );

    private static final List<String> SCHEMAS_DE_LISTAGEM_PERMITIDOS = List.of(
            "saude", "assistencia_social", "educacao"
    );

    private static String ordenarReferencia(String alias) {
        return String.format(EXPRESSAO_ORDEM_REFERENCIA, alias, alias, alias);
    }

    public String principaisInformacoesMunicipal(
            @Size(min = 1, max = 100, message = "Municipio deve conter entre 1 e 100 caracteres")
            @NotBlank(message = "Municipio nao pode ser em branco")
            String municipio) {
        return informacoesRepository.buscarDadosPrincipaisDoMunicipio(municipio);
    }

    public List<String> tabelasDoSchema(String schema) {
        validarSchemaParaListagem(schema);
        return informacoesRepository.buscarTabelasDoSchema(schema);
    }

    public List<String> buscarEvolucaoDoIndicador(String schema, String indicador, String municipio) {
        validarSchemaDinamico(schema);
        validarIndicador(indicador);

        String sql = String.format(
                "SELECT json_build_object('Referência', x.referencia, 'Valor', x.valor) " +
                        "FROM %s.%s x " +
                        "JOIN dados_estadual.referencias_codigos_municipais i ON i.codigo_ibge = x.cod_municipio " +
                        "WHERE i.municipio = ? " +
                        "AND x.valor IS NOT NULL " +
                        "AND x.valor >= 0 " +
                        "ORDER BY %s, x.referencia",
                schema, indicador, ordenarReferencia("x")
        );

        return jdbcTemplate.queryForList(sql, String.class, municipio);
    }

    public List<String> buscarResumoDoIndicador(String schema, String indicador, String municipio) {
        validarSchemaDinamico(schema);
        validarIndicador(indicador);

        String sql = String.format(
                "WITH dados AS (" +
                        "    SELECT " +
                        "        x.valor, " +
                        "        x.referencia, " +
                        "        FIRST_VALUE(x.valor) OVER (ORDER BY %s DESC, x.referencia DESC) as valor_recente, " +
                        "        FIRST_VALUE(x.referencia) OVER (ORDER BY %s DESC, x.referencia DESC) as ref_recente, " +
                        "        FIRST_VALUE(x.fonte) OVER (ORDER BY %s DESC, x.referencia DESC) as fonte_recente, " +
                        "        FIRST_VALUE(x.referencia) OVER (ORDER BY x.valor DESC, %s DESC, x.referencia DESC) as melhor_ano, " +
                        "        MAX(x.valor) OVER () as maior_valor, " +
                        "        FIRST_VALUE(x.referencia) OVER (ORDER BY x.valor ASC, %s DESC, x.referencia DESC) as pior_ano, " +
                        "        MIN(x.valor) OVER () as menor_valor, " +
                        "        FIRST_VALUE(x.valor) OVER (ORDER BY %s ASC, x.referencia ASC) as primeiro_valor, " +
                        "        FIRST_VALUE(x.referencia) OVER (ORDER BY %s ASC, x.referencia ASC) as primeiro_ano " +
                        "    FROM dados_estadual.referencias_codigos_municipais i " +
                        "    JOIN %s.%s x ON i.codigo_ibge = x.cod_municipio " +
                        "    WHERE i.municipio = ? " +
                        "    AND x.valor IS NOT NULL " +
                        "    AND x.valor >= 0 " +
                        ") " +
                        "SELECT json_build_object(" +
                        "    'VALOR MAIS RECENTE', d.valor_recente," +
                        "    'Referência', d.ref_recente," +
                        "    'Fonte dos Dados', d.fonte_recente," +
                        "    'MELHOR RESULTADO', d.melhor_ano," +
                        "    'Maior valor', d.maior_valor," +
                        "    'PONTO DE ATENÇÃO', d.pior_ano," +
                        "    'Menor valor', d.menor_valor," +
                        "    'VARIAÇÃO NO PERÍODO', CONCAT(CASE WHEN (d.valor_recente - d.primeiro_valor) >= 0 THEN '+' ELSE '' END, (d.valor_recente - d.primeiro_valor), ' p.p.')," +
                        "    'Evolução', CONCAT('de ', d.primeiro_ano, ' a ', d.ref_recente)" +
                        ")::text FROM dados d LIMIT 1",
                ordenarReferencia("x"),
                ordenarReferencia("x"),
                ordenarReferencia("x"),
                ordenarReferencia("x"),
                ordenarReferencia("x"),
                ordenarReferencia("x"),
                ordenarReferencia("x"),
                schema, indicador
        );

        return jdbcTemplate.queryForList(sql, String.class, municipio);
    }

    public List<String> buscarComparacaoTerritorial(String schema, String indicador, List<String> municipios) {
        validarSchemaDinamico(schema);
        validarIndicador(indicador);

        String sql = String.format(
                "WITH dados_filtrados AS (" +
                        "    SELECT " +
                        "        i.municipio," +
                        "        x.valor," +
                        "        x.referencia," +
                        "        %s as ordem_referencia," +
                        "        MAX(%s) OVER() as ultima_ordem_referencia" +
                        "    FROM %s.%s x " +
                        "    JOIN dados_estadual.referencias_codigos_municipais i ON i.codigo_ibge = x.cod_municipio " +
                        "    WHERE i.municipio IN (:municipios)" +
                        "    AND x.valor IS NOT NULL " +
                        "    AND x.valor >= 0 " +
                        ") " +
                        "SELECT json_build_object(" +
                        "    'Município', df.municipio," +
                        "    'Valor', df.valor," +
                        "    'Referência', df.referencia" +
                        ")::text " +
                        "FROM dados_filtrados df " +
                        "WHERE df.ordem_referencia = df.ultima_ordem_referencia " +
                        "ORDER BY df.valor DESC",
                ordenarReferencia("x"), ordenarReferencia("x"), schema, indicador
        );

        Map<String, Object> params = new HashMap<>();
        params.put("municipios", municipios);

        return namedParameterJdbcTemplate.queryForList(sql, params, String.class);
    }

    public List<ClusterizacaoDTO.PontoMunicipal> buscarDadosParaClusterizacao(
            String schema,
            String indicador
    ) {
        validarSchemaDinamico(schema);
        validarIndicador(indicador);

        String sql = String.format(
                "SELECT i.municipio, AVG(x.valor)::double precision AS valor " +
                        "FROM %s.%s x " +
                        "JOIN dados_estadual.referencias_codigos_municipais i " +
                        "ON i.codigo_ibge = x.cod_municipio " +
                        "WHERE x.referencia = (" +
                        "    SELECT t.referencia FROM %s.%s t WHERE t.valor IS NOT NULL AND t.valor >= 0 " +
                        "    ORDER BY %s DESC, t.referencia DESC LIMIT 1" +
                        ") " +
                        "AND x.valor IS NOT NULL " +
                        "AND x.valor >= 0 " +
                        "GROUP BY i.municipio " +
                        "ORDER BY i.municipio",
                schema, indicador, schema, indicador, ordenarReferencia("t")
        );

        return jdbcTemplate.query(
                sql,
                (resultado, linha) -> new ClusterizacaoDTO.PontoMunicipal(
                        resultado.getString("municipio"),
                        resultado.getDouble("valor")
                )
        );
    }

    public String buscarReferenciaMaisRecente(String schema, String indicador) {
        validarSchemaDinamico(schema);
        validarIndicador(indicador);

        String sql = String.format(
                "SELECT t.referencia FROM %s.%s t WHERE t.valor IS NOT NULL AND t.valor >= 0 " +
                        "ORDER BY %s DESC, t.referencia DESC LIMIT 1",
                schema,
                indicador,
                ordenarReferencia("t")
        );

        return jdbcTemplate.queryForObject(sql, String.class);
    }

    private void validarSchemaDinamico(String schema) {
        if (!SCHEMAS_DINAMICOS_PERMITIDOS.contains(schema)) {
            throw new IllegalArgumentException("Schema inválido");
        }
    }

    private void validarSchemaParaListagem(String schema) {
        if (!SCHEMAS_DE_LISTAGEM_PERMITIDOS.contains(schema)) {
            throw new IllegalArgumentException("Schema inválido");
        }
    }

    private void validarIndicador(String indicador) {
        if (!indicador.matches("[a-zA-Z0-9_]+")) {
            throw new IllegalArgumentException("Indicador inválido");
        }
    }
}

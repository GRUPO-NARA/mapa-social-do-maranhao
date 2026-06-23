package mpma.mapa.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import mpma.mapa.service.InformacoesService;
import mpma.mapa.service.Resposta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Validated
@RestController
@RequestMapping("/informacoes")
@RateLimiter(name = "RateLimiter")
@Tag(name = "Informações", description = "Endpoints para dados informativos")
public class InformacoesController {

    @Autowired
    private InformacoesService informacoesService;

    @Autowired
    private Resposta resposta;

    @Autowired
    private ObjectMapper objectMapper;

    @Operation(summary = "Busca os principais dados do município",
            description = "Retorna um conjunto de informações relevantes sobre o município especificado, incluindo dados demográficos, econômicos e geográficos, com base nos dados mais recentes disponíveis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Informações do município encontradas e retornadas com sucesso!",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Exemplo de requisição bem sucedida para as informações do município de São Luís",
                                            value = """
                                                    {
                                                      "Resposta da Requisição": "[{\\"Quantidade de Pessoas\\" : 1037775, \\"Referência dos dados\\" : \\"2022\\", \\"Fonte dos Dados\\" : \\"SIDRA\\"}, {\\"Quantidade de Homens\\" : 483501, \\"Referência dos dados\\" : \\"2022\\", \\"Fonte dos Dados\\" : \\"SIDRA\\"}, {\\"Quantidade de Mulheres\\" : 554274, \\"Referência dos dados\\" : \\"2022\\", \\"Fonte dos Dados\\" : \\"SIDRA\\"}, {\\"Densidade Demográfica\\" : 1779.87, \\"Referência dos dados\\" : \\"2022\\", \\"Fonte dos Dados\\" : \\"SIDRA\\"}, {\\"Área Territorial\\" : 583.063, \\"Referência dos dados\\" : \\"2022\\", \\"Fonte dos Dados\\" : \\"SIDRA\\"}]",
                                                      "Status da Requisição": "200",
                                                      "Indicador da Requisição": "Dados Principais do município de São Luís"
                                                    }"""
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "204",
                    description = "Não foram encontrados dados do município informado"),
            @ApiResponse(responseCode = "400",
                    description = "Parâmetro município não informado ou inválido"),
            @ApiResponse(responseCode = "404",
                    description = "Endpoint não encontrado"),
            @ApiResponse(responseCode = "500",
                    description = "Erro na requisição dos dados")
    })
    @GetMapping("/dadosPrincipaisMunicipal")
    public ResponseEntity<HashMap<String, Object>> DadosPrincipaisMunicipal(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @RequestParam("municipio")
            @NotBlank(message = "Município não pode ser em branco")
            @Size(max = 100, message = "Município deve conter no máximo 100 caracteres")
            String municipio) {
        HashMap<String, Object> respostaDaRequisicao = resposta.CorpoDaResposta(
                "Dados Principais do município de " + municipio,
                informacoesService.principaisInformacoesMunicipal(municipio),
                "200"
        );
        return ResponseEntity.ok().body(respostaDaRequisicao);
    }

    @GetMapping("/tabelasDoSchema")
    public ResponseEntity<HashMap<String, Object>> BuscarTabelasDoSchema(
            @Parameter(
                    name = "schema",
                    description = "nome do schema (ex: demograficos)",
                    example = "demograficos",
                    required = true
            )
            @RequestParam("schema")
            @NotBlank(message = "Schema não pode ser em branco")
            @Pattern(regexp = "[a-zA-Z0-9_]+", message = "Schema contém caracteres inválidos")
            String schema) {
        HashMap<String, Object> respostaDaRequisicao = resposta.CorpoDaResposta(
                "Tabelas do schema " + schema,
                informacoesService.tabelasDoSchema(schema),
                "200"
        );
        return ResponseEntity.ok().body(respostaDaRequisicao);
    }

    @GetMapping("/evolucaoDoIndicador")
    public ResponseEntity<HashMap<String, Object>> BuscarEvolucaoDoIndicador(
            @Parameter(
                    name = "schema",
                    description = "nome do schema onde o indicador está localizado (ex: demograficos)",
                    example = "demograficos",
                    required = true
            )
            @RequestParam("schema")
            @NotBlank(message = "Schema não pode ser em branco")
            @Pattern(regexp = "[a-zA-Z0-9_]+", message = "Schema contém caracteres inválidos")
            String schema,
            @Parameter(
                    name = "indicador",
                    description = "nome do indicador para o qual se deseja obter a evolução histórica (ex: quantidade_de_homens)",
                    example = "quantidade_de_homens",
                    required = true
            )
            @RequestParam("indicador")
            @NotBlank(message = "Indicador não pode ser em branco")
            @Pattern(regexp = "[a-zA-Z0-9_]+", message = "Indicador contém caracteres inválidos")
            String indicador,
            @Parameter(
                    name = "municipio",
                    description = "nome do município para o qual se deseja obter a evolução histórica do indicador (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @RequestParam("municipio")
            @NotBlank(message = "Município não pode ser em branco")
            @Size(max = 100, message = "Município deve conter no máximo 100 caracteres")
            String municipio
    ) {
        HashMap<String, Object> respostaDaRequisicao = resposta.CorpoDaResposta(
                "Evolução histórica do indicador " + indicador + " para o município de " + municipio,
                informacoesService.buscarEvolucaoDoIndicador(schema, indicador, municipio),
                "200"
        );
        return ResponseEntity.ok().body(respostaDaRequisicao);
    }

    @GetMapping("/resumoDoIndicador")
    public ResponseEntity<HashMap<String, Object>> BuscarResumoDoIndicador(
            @Parameter(
                    name = "schema",
                    description = "nome do schema onde o indicador está localizado (ex: demograficos)",
                    example = "demograficos",
                    required = true
            )
            @RequestParam("schema")
            @NotBlank(message = "Schema não pode ser em branco")
            @Pattern(regexp = "[a-zA-Z0-9_]+", message = "Schema contém caracteres inválidos")
            String schema,
            @Parameter(
                    name = "indicador",
                    description = "nome do indicador para o qual se deseja obter o resumo dos cards (ex: quantidade_de_homens)",
                    example = "quantidade_de_homens",
                    required = true
            )
            @RequestParam("indicador")
            @NotBlank(message = "Indicador não pode ser em branco")
            @Pattern(regexp = "[a-zA-Z0-9_]+", message = "Indicador contém caracteres inválidos")
            String indicador,
            @Parameter(
                    name = "municipio",
                    description = "nome do município para o qual se deseja obter o resumo do indicador (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @RequestParam("municipio")
            @NotBlank(message = "Município não pode ser em branco")
            @Size(max = 100, message = "Município deve conter no máximo 100 caracteres")
            String municipio
    ) {
        List<String> resultadoBanco = informacoesService.buscarResumoDoIndicador(schema, indicador, municipio);
        Object dadosFormatados = null;

        if (resultadoBanco != null && !resultadoBanco.isEmpty()) {
            try {
                dadosFormatados = objectMapper.readValue(resultadoBanco.get(0), Object.class);
            } catch (JsonProcessingException e) {
                dadosFormatados = resultadoBanco.get(0);
            }
        }

        HashMap<String, Object> respostaDaRequisicao = resposta.CorpoDaResposta(
                "Resumo estatístico do indicador " + indicador + " para o município de " + municipio,
                dadosFormatados,
                "200"
        );
        return ResponseEntity.ok().body(respostaDaRequisicao);
    }

    @GetMapping("/comparacaoTerritorial")
    public ResponseEntity<HashMap<String, Object>> BuscarComparacaoTerritorial(
            @RequestParam("schema")
            @NotBlank String schema,

            @RequestParam("indicador")
            @NotBlank String indicador,

            @RequestParam("municipios")
            @NotEmpty List<String> municipios
    ) {
        List<String> resultadoBanco = informacoesService.buscarComparacaoTerritorial(schema, indicador, municipios);
        List<Object> dadosFormatados = new ArrayList<>();

        if (resultadoBanco != null) {
            for (String jsonBruto : resultadoBanco) {
                try {
                    dadosFormatados.add(objectMapper.readValue(jsonBruto, Object.class));
                } catch (JsonProcessingException e) {
                    dadosFormatados.add(jsonBruto);
                }
            }
        }

        HashMap<String, Object> respostaDaRequisicao = resposta.CorpoDaResposta(
                "Comparação territorial do indicador " + indicador,
                dadosFormatados,
                "200"
        );
        return ResponseEntity.ok().body(respostaDaRequisicao);
    }

}
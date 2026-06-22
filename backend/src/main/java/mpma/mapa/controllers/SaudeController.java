package mpma.mapa.controllers;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import mpma.mapa.service.Saude.SaudeService;
import mpma.mapa.service.Resposta;

import java.util.HashMap;

@RestController
@RequestMapping("/saude")
@RateLimiter(name = "RateLimiter")
@Tag(name = "Saúde", description = "Endpoints relacionados a dados de saúde dos municípios do Maranhão.")
public class SaudeController {

    @Autowired
    private Resposta resposta;

    @Autowired
    private SaudeService saudeService;

    @Operation(
            summary = "Busca a idade mediana do município",
            description = "Retorna o valor da idade mediana da população com base no município informado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Idade mediana encontrada com sucesso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "Resposta da Requisição": "{\\"Idade Mediana\\" : 33, \\"Referência dos Dados\\" : \\"2022\\", \\"Fonte dos Dados\\" : \\"SIDRA\\"}",
                      "Status da Requisição": "200",
                      "Indicador da Requisição": "Idade Mediana do Município de São Luís"
                    }"""))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/idadeMediana")
    public ResponseEntity<HashMap<String, Object>> BuscarIdadeMediana(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ) {
        HashMap<String, Object> respostaDaRequisicao = resposta.CorpoDaResposta(
                "Idade Mediana do Município de " + municipio,
                saudeService.idadeMedianaMunicipal(municipio),
                "200"
        );
        return ResponseEntity.ok().body(respostaDaRequisicao);
    }

    @Operation(
            summary = "Busca os casos de Hanseníase do município",
            description = "Retorna a quantidade registrada de casos de Hanseníase do município informado, com base no ano mais recente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Casos de Hanseníase encontrados com sucesso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "Resposta da Requisição": "{\\"Casos de Hanseníase\\" : 45, \\"Referência dos Dados\\" : \\"2023\\", \\"Fonte dos Dados\\" : \\"SES-MA\\"}",
                      "Status da Requisição": "200",
                      "Indicador da Requisição": "Casos de Hanseníase do Município de São Luís"
                    }"""))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/casosHanseniase")
    public ResponseEntity<HashMap<String, Object>> BuscarCasosHanseniase(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ) {
        HashMap<String, Object> respostaDaRequisicao = resposta.CorpoDaResposta(
                "Casos de Hanseníase do Município de " + municipio,
                saudeService.casosHanseniaseMunicipal(municipio),
                "200"
        );
        return ResponseEntity.ok().body(respostaDaRequisicao);
    }

    @Operation(
            summary = "Busca os casos de Tuberculose do município",
            description = "Retorna a quantidade registrada de casos de Tuberculose do município informado, com base no ano mais recente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Casos de Tuberculose encontrados com sucesso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "Resposta da Requisição": "{\\"Casos de Tuberculose\\" : 28, \\"Referência dos Dados\\" : \\"2023\\", \\"Fonte dos Dados\\" : \\"SES-MA\\"}",
                      "Status da Requisição": "200",
                      "Indicador da Requisição": "Casos de Tuberculose do Município de São Luís"
                    }"""))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/casosTuberculose")
    public ResponseEntity<HashMap<String, Object>> BuscarCasosTuberculose(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ) {
        HashMap<String, Object> respostaDaRequisicao = resposta.CorpoDaResposta(
                "Casos de Tuberculose do Município de " + municipio,
                saudeService.casosTuberculoseMunicipal(municipio),
                "200"
        );
        return ResponseEntity.ok().body(respostaDaRequisicao);
    }

    @Operation(
            summary = "Busca a cobertura da Estratégia de Saúde Familiar do município",
            description = "Retorna o percentual de cobertura da Estratégia de Saúde da Família (ESF) para o município especificado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Percentual de cobertura encontrado com sucesso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "Resposta da Requisição": "{\\"Cobertura da Estratégia de Saúde Familiar\\" : 85.4, \\"Referência dos Dados\\" : \\"2023\\", \\"Fonte dos Dados\\" : \\"MS-EGESTOR\\"}",
                      "Status da Requisição": "200",
                      "Indicador da Requisição": "Cobertura da Estratégia de Saúde Familiar do Município de São Luís"
                    }"""))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/coberturaEstrategiaSaudeFamiliar")
    public ResponseEntity<HashMap<String, Object>> BuscarCoberturaEstrategiaSaudeFamiliar(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ) {
        HashMap<String, Object> respostaDaRequisicao = resposta.CorpoDaResposta(
                "Cobertura da Estratégia de Saúde Familiar do Município de " + municipio,
                saudeService.coberturaDaEstrategiaDeSaudeFamiliarMunicipal(municipio),
                "200"
        );
        return ResponseEntity.ok().body(respostaDaRequisicao);
    }

    @Operation(
            summary = "Busca a cobertura de Agentes Comunitários de Saúde do município",
            description = "Retorna o percentual de cobertura dos Agentes Comunitários de Saúde (ACS) para o município especificado, baseando-se no ano histórico mais recente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Percentual de cobertura de ACS encontrado com sucesso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "Resposta da Requisição": "{\\"Cobertura de Agentes Comunitários de Saúde\\" : 92.1, \\"Referência dos Dados\\" : \\"2023\\", \\"Fonte dos Dados\\" : \\"MS-EGESTOR\\"}",
                      "Status da Requisição": "200",
                      "Indicador da Requisição": "Cobertura de Agentes Comunitários de Saúde do Município de São Luís"
                    }"""))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/coberturaAgentesComunitarios")
    public ResponseEntity<HashMap<String, Object>> BuscarCoberturaAgentesComunitarios(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ) {
        HashMap<String, Object> respostaDaRequisicao = resposta.CorpoDaResposta(
                "Cobertura de Agentes Comunitários de Saúde do Município de " + municipio,
                saudeService.coberturaDeAgentesComunitariosDeSaudeMunicipal(municipio),
                "200"
        );
        return ResponseEntity.ok().body(respostaDaRequisicao);
    }
}
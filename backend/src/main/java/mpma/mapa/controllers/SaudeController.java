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

    @Operation(summary = "Busca a idade mediana do município")
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
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Idade Mediana do Município de " + municipio, saudeService.idadeMedianaMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca os casos de Hanseníase do município")
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
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Casos de Hanseníase do Município de " + municipio, saudeService.casosHanseniaseMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca os casos de Tuberculose do município")
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
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Casos de Tuberculose do Município de " + municipio, saudeService.casosTuberculoseMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca a cobertura da Estratégia de Saúde Familiar do município")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Percentual de cobertura encontrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
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
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Cobertura da Estratégia de Saúde Familiar do Município de " + municipio, saudeService.coberturaDaEstrategiaDeSaudeFamiliarMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca a cobertura de Agentes Comunitários de Saúde do município")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Percentual de cobertura encontrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
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
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Cobertura de Agentes Comunitários de Saúde do Município de " + municipio, saudeService.coberturaDeAgentesComunitariosDeSaudeMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca a cobertura vacinal do município")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cobertura vacinal encontrada", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "Resposta da Requisição": "{\\"Cobertura Vacinal\\" : 78.3, \\"Referência dos Dados\\" : \\"2023\\", \\"Fonte dos Dados\\" : \\"SIPNI\\"}",
                      "Status da Requisição": "200",
                      "Indicador da Requisição": "Cobertura Vacinal do Município de São Luís"
                    }""")))
    })
    @GetMapping("/coberturaVacina")
    public ResponseEntity<HashMap<String, Object>> BuscarCoberturaVacina(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ) {
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Cobertura Vacinal do Município de " + municipio, saudeService.coberturaVacinaMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca os dados de desnutrição infantil (1 ano de idade) do município")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados encontrados com sucesso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "Resposta da Requisição": "{\\"Desnutrição Infantil um ano de idade\\" : 4.2, \\"Referência dos Dados\\" : \\"2023\\", \\"Fonte dos Dados\\" : \\"SISVAN\\"}",
                      "Status da Requisição": "200",
                      "Indicador da Requisição": "Desnutrição Infantil um ano de idade do Município de São Luís"
                    }""")))
    })
    @GetMapping("/desnutricaoInfantilUmAno")
    public ResponseEntity<HashMap<String, Object>> BuscarDesnutricaoInfantilUmAno(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ) {
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Desnutrição Infantil um ano de idade do Município de " + municipio, saudeService.desnutricaoInfantilUmAnoDeIdadeMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca as internações por Diabetes Mellitus do município")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Internações encontradas com sucesso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "Resposta da Requisição": "{\\"Internações Diabetes Mellitus\\" : 112, \\"Referência dos Dados\\" : \\"2023\\", \\"Fonte dos Dados\\" : \\"SIH-SUS\\"}",
                      "Status da Requisição": "200",
                      "Indicador da Requisição": "Internações por Diabetes Mellitus do Município de São Luís"
                    }""")))
    })
    @GetMapping("/internacoesDiabetes")
    public ResponseEntity<HashMap<String, Object>> BuscarInternacoesDiabetes(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ) {
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Internações por Diabetes Mellitus do Município de " + municipio, saudeService.internacoesDiabetesMellitusMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca as internações por Hipertensão Primária do município")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Internações encontradas com sucesso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "Resposta da Requisição": "{\\"Internações Hipertensão Primária\\" : 89, \\"Referência dos Dados\\" : \\"2023\\", \\"Fonte dos Dados\\" : \\"SIH-SUS\\"}",
                      "Status da Requisição": "200",
                      "Indicador da Requisição": "Internações por Hipertensão Primária do Município de São Luís"
                    }""")))
    })
    @GetMapping("/internacoesHipertensao")
    public ResponseEntity<HashMap<String, Object>> BuscarInternacoesHipertensao(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ) {
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Internações por Hipertensão Primária do Município de " + municipio, saudeService.internacoesHipertensaoPrimariaMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca o número de nascidos vivos de mães adolescentes no município")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados encontrados com sucesso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "Resposta da Requisição": "{\\"Nascidos Vivos de Mães Adolescentes\\" : 240, \\"Referência dos Dados\\" : \\"2023\\", \\"Fonte dos Dados\\" : \\"SINASC\\"}",
                      "Status da Requisição": "200",
                      "Indicador da Requisição": "Nascidos Vivos de Mães Adolescentes do Município de São Luís"
                    }""")))
    })
    @GetMapping("/nascidosVivosMaesAdolescentes")
    public ResponseEntity<HashMap<String, Object>> BuscarNascidosVivosMaesAdolescentes(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ) {
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Nascidos Vivos de Mães Adolescentes do Município de " + municipio, saudeService.nascidosVivosMaesAdolescentesMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca o número de Centros de Saúde do município")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Centros de saúde contabilizados com sucesso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "Resposta da Requisição": "{\\"Número de Centros de Saúde\\" : 18, \\"Referência dos Dados\\" : \\"2024\\", \\"Fonte dos Dados\\" : \\"CNES\\"}",
                      "Status da Requisição": "200",
                      "Indicador da Requisição": "Número de Centros de Saúde do Município de São Luís"
                    }""")))
    })
    @GetMapping("/numeroCentrosSaude")
    public ResponseEntity<HashMap<String, Object>> BuscarNumeroCentrosSaude(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ) {
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Número de Centros de Saúde do Município de " + municipio, saudeService.numeroCentroSaudeMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca o número de Hospitais Gerais do município")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hospitais gerais contabilizados com sucesso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "Resposta da Requisição": "{\\"Número de Hospitais Gerais\\" : 5, \\"Referência dos Dados\\" : \\"2024\\", \\"Fonte dos Dados\\" : \\"CNES\\"}",
                      "Status da Requisição": "200",
                      "Indicador da Requisição": "Número de Hospitais Gerais do Município de São Luís"
                    }""")))
    })
    @GetMapping("/numeroHospitaisGerais")
    public ResponseEntity<HashMap<String, Object>> BuscarNumeroHospitaisGerais(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ) {
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Número de Hospitais Gerais do Município de " + municipio, saudeService.numeroDeHospitaisGeraisMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca o número total de leitos de internação do município")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leitos contabilizados com sucesso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "Resposta da Requisição": "{\\"Número de Leitos de Internação\\" : 1240, \\"Referência dos Dados\\" : \\"2024\\", \\"Fonte dos Dados\\" : \\"CNES\\"}",
                      "Status da Requisição": "200",
                      "Indicador da Requisição": "Número de Leitos de Internação do Município de São Luís"
                    }""")))
    })
    @GetMapping("/numeroLeitosInternacao")
    public ResponseEntity<HashMap<String, Object>> BuscarNumeroLeitosInternacao(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ) {
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Número de Leitos de Internação do Município de " + municipio, saudeService.numeroLeitosInternacaoMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca o número total de médicos atuantes no município")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Médicos contabilizados com sucesso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "Resposta da Requisição": "{\\"Número Total de Médicos\\" : 2450, \\"Referência dos Dados\\" : \\"2024\\", \\"Fonte dos Dados\\" : \\"CNES\\"}",
                      "Status da Requisição": "200",
                      "Indicador da Requisição": "Número Total de Médicos do Município de São Luís"
                    }""")))
    })
    @GetMapping("/numeroMedicosTotal")
    public ResponseEntity<HashMap<String, Object>> BuscarNumeroMedicosTotal(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ) {
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Número Total de Médicos do Município de " + municipio, saudeService.numeroMedicosTotalMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca o número de leitos de urgência do município")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leitos de urgência contabilizados com sucesso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "Resposta da Requisição": "{\\"Número de Leitos de Urgência\\" : 180, \\"Referência dos Dados\\" : \\"2024\\", \\"Fonte dos Dados\\" : \\"CNES\\"}",
                      "Status da Requisição": "200",
                      "Indicador da Requisição": "Número de Leitos de Urgência do Município de São Luís"
                    }""")))
    })
    @GetMapping("/numeroLeitosUrgencia")
    public ResponseEntity<HashMap<String, Object>> BuscarNumeroLeitosUrgencia(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ) {
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Número de Leitos de Urgência do Município de " + municipio, saudeService.numerosLeitosUrgenciaMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca o número de óbitos por Diabetes Mellitus do município")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Óbitos por diabetes encontrados", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "Resposta da Requisição": "{\\"Óbitos por Diabetes Mellitus\\" : 42, \\"Referência dos Dados\\" : \\"2023\\", \\"Fonte dos Dados\\" : \\"SIM\\"}",
                      "Status da Requisição": "200",
                      "Indicador da Requisição": "Óbitos por Diabetes Mellitus do Município de São Luís"
                    }""")))
    })
    @GetMapping("/obitosDiabetes")
    public ResponseEntity<HashMap<String, Object>> BuscarObitosDiabetes(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ) {
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Óbitos por Diabetes Mellitus do Município de " + municipio, saudeService.obitosDiabetesMellitusMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca o número de óbitos por doenças cerebrovasculares do município")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Óbitos por doenças cerebrovasculares encontrados", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "Resposta da Requisição": "{\\"Óbitos por Doenças Cerebrovasculares\\" : 67, \\"Referência dos Dados\\" : \\"2023\\", \\"Fonte dos Dados\\" : \\"SIM\\"}",
                      "Status da Requisição": "200",
                      "Indicador da Requisição": "Óbitos por Doenças Cerebrovasculares do Município de São Luís"
                    }""")))
    })
    @GetMapping("/obitosCerebrovasculares")
    public ResponseEntity<HashMap<String, Object>> BuscarObitosCerebrovasculares(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ) {
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Óbitos por Doenças Cerebrovasculares do Município de " + municipio, saudeService.obitosDoencasCerebrovascularesMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca a taxa de mortalidade infantil do município")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Taxa de mortalidade infantil encontrada", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "Resposta da Requisição": "{\\"Taxa de Mortalidade Infantil\\" : 12.5, \\"Referência dos Dados\\" : \\"2023\\", \"Fonte dos Dados\\" : \\"SIM\\"}",
                      "Status da Requisição": "200",
                      "Indicador da Requisição": "Taxa de Mortalidade Infantil do Município de São Luís"
                    }"""))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/taxaMortalidadeInfantil")
    public ResponseEntity<HashMap<String, Object>> BuscarTaxaDeMortalidadeInfantilMunicipal(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ) {
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Taxa de Mortalidade Infantil do Município de " + municipio, saudeService.taxaMortalidadeInfantilMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca a taxa de mortalidade infantil do estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Taxa de mortalidade infantil encontrada", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {
                      "Resposta da Requisição": "{\\"Taxa de Mortalidade Infantil\\" : 10.2, \\"Referência dos Dados\\" : \\"2023\\", \"Fonte dos Dados\\" : \\"SIM\\"}",
                      "Status da Requisição": "200",
                      "Indicador da Requisição": "Taxa de Mortalidade Infantil do Estado do Maranhão"
                    }"""))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/taxaMortalidadeInfantilEstadual")
    public ResponseEntity<HashMap<String, Object>> BuscarTaxaDeMortalidadeInfantilEstadual() {
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Taxa de Mortalidade Infantil do Estado do Maranhão", saudeService.taxaMortalidadeInfantilEstadual(), "200"));
    }
}
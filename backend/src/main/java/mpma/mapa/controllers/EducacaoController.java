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
import mpma.mapa.service.Educacao.EducacaoService;
import mpma.mapa.service.Resposta;

import java.util.HashMap;

@RestController
@RequestMapping("/educacao")
@RateLimiter(name = "RateLimiter")
@Tag(name = "Educação", description = "Endpoints relacionados a dados de educação dos municípios do Maranhão.")
public class EducacaoController {

    @Autowired
    private Resposta resposta;

    @Autowired
    private EducacaoService educacaoService;

    @Operation(
            summary = "Busca a taxa de analfabetismo de 15 anos ou mais do município",
            description = "Retorna o percentual de pessoas com 15 anos ou mais que são analfabetas no município cadastrado."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Taxa de analfabetismo encontrada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                            {
                              "Resposta da Requisição": "{\\"Taxa de Analfabetismo 15 anos ou mais\\" : 12.3, \\"Referência dos Dados\\" : \\"2023\\", \\"Fonte dos Dados\\" : \\"IBGE\\"}",
                              "Status da Requisição": "200",
                              "Indicador da Requisição": "Taxa de Analfabetismo de 15 anos ou mais do Município de São Luís"
                            }"""))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })

    @GetMapping("/taxaAnalfabetismo15AnosOuMais")
    public ResponseEntity<HashMap<String, Object>> BuscarTaxaDeAnalfabetismo15AnosOuMais(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ) {
        return ResponseEntity.ok().body(
                resposta.CorpoDaResposta(
                        "Taxa de Analfabetismo de 15 anos ou mais do Município de " + municipio,
                        educacaoService.taxaDeAnalfabetismo15AnosOuMaisMunicipal(municipio),
                        "200"
                )
        );
    }

    @Operation(
            summary = "Busca a taxa de analfabetismo de 15 anos ou mais do estado",
            description = "Retorna o percentual de pessoas com 15 anos ou mais que são analfabetas no estado do Maranhão."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Taxa de analfabetismo encontrada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                            {
                              "Resposta da Requisição": "{\\"Taxa de Analfabetismo 15 anos ou mais\\" : 10.5, \\"Referência dos Dados\\" : \\"2023\\", \\"Fonte dos Dados\\" : \\"IBGE\\"}",
                              "Status da Requisição": "200",
                              "Indicador da Requisição": "Taxa de Analfabetismo de 15 anos ou mais do Estado do Maranhão"
                            }"""))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/taxaAnalfabetismo15AnosOuMaisEstadual")
    public ResponseEntity<HashMap<String, Object>> BuscarTaxaDeAnalfabetismo15AnosOuMaisEstadual() {
        return ResponseEntity.ok().body(
                resposta.CorpoDaResposta(
                        "Taxa de Analfabetismo de 15 anos ou mais do Estado do Maranhão",
                        educacaoService.taxaDeAnalfabetismo15AnosOuMaisEstadual(),
                        "200"
                )
        );
    }

    @Operation(
            summary = "Busca a taxa de aprovação no Ensino Fundamental do município",
            description = "Retorna o percentual de alunos aprovados no Ensino Fundamental no município cadastrado."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Taxa de aprovação no ensino fundamental encontrada com sucesso",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "Resposta da Requisição": "{\\"Taxa de Aprovação no Ensino Fundamental\\" : 88.5, \\"Referência dos Dados\\" : \\"2023\\", \\"Fonte dos Dados\\" : \\"INEP\\"}",
                              "Status da Requisição": "200",
                              "Indicador da Requisição": "Taxa de Aprovação no Ensino Fundamental do Município de São Luís"
                            }"""))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/taxaAprovacaoEnsinoFundamental")
    public ResponseEntity<HashMap<String, Object>> BuscarTaxaAprovacaoEnsinoFundamental(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ) {
        return ResponseEntity.ok().body(
                resposta.CorpoDaResposta(
                        "Taxa de Aprovação no Ensino Fundamental do Município de " + municipio,
                        educacaoService.taxaDeAprovacaoNoEnsinoFundamentalMunicipal(municipio),
                        "200"
                )
        );
    }

    @Operation(
            summary = "Busca a taxa de aprovação no Ensino Médio do município",
            description = "Retorna o percentual de alunos aprovados no Ensino Médio no município cadastrado."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Taxa de aprovação no ensino médio encontrada com sucesso",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "Resposta da Requisição": "{\\"Taxa de Aprovação no Ensino Médio\\" : 82.1, \\"Referência dos Dados\\" : \\"2023\\", \\"Fonte dos Dados\\" : \\"INEP\\"}",
                              "Status da Requisição": "200",
                              "Indicador da Requisição": "Taxa de Aprovação no Ensino Médio do Município de São Luís"
                            }"""))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/taxaAprovacaoEnsinoMedio")
    public ResponseEntity<HashMap<String, Object>> BuscarTaxaAprovacaoEnsinoMedio(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ) {
        return ResponseEntity.ok().body(
                resposta.CorpoDaResposta(
                        "Taxa de Aprovação no Ensino Médio do Município de " + municipio,
                        educacaoService.taxaDeAprovacaoNoEnsinoMedioMunicipal(municipio),
                        "200"
                )
        );
    }
}
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
import mpma.mapa.service.Resposta;
import mpma.mapa.service.Geograficos.GeograficosService;

import java.util.HashMap;


@RestController
@RequestMapping("/geograficos")
@RateLimiter(name = "RateLimiter")
@Tag(name = "Geográficos", description = "Endpoints para dados geográficos")
public class GeograficosController {

    @Autowired
    private GeograficosService geograficosService;

    @Autowired
    private Resposta resposta;

    @Operation(summary = "Busca a área total do município",
            description = "Retorna o valor da área total (em km²) do município com base no nome do município informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "Área territorial encontrada e retornada com sucesso!",
            content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(
                                    name = "Exemplo de requisição bem sucedida para a área territorial do município de São Luís",
                                    value = "{\n" +
                                            "  \"indicador\": \"Área Total do Município de São Luís\",\n" +
                                            "  \"resposta\": {\n" +
                                            "    \"valor\": 583.063\n" +
                                            "  },\n" +
                                            "  \"status\": \"200\"\n" +
                                            "}"
                            )
                    }
            )),
            @ApiResponse(responseCode = "204",
            description = "Área territorial não encontrada"),
            @ApiResponse(responseCode = "400",
            description = "Parâmetro município não informado ou inválido"),
            @ApiResponse(responseCode = "404",
            description = "Endpoint não encontrado"),
            @ApiResponse(responseCode = "500",
            description = "Erro na requisição dos dados")
    })
    @GetMapping("/areaTotal")
    public ResponseEntity<HashMap<String, Object>> BuscarAreaTerritorial(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @Valid @RequestParam("municipio") String municipio){
        HashMap<String, Object> respostaDaRequisicao = resposta.CorpoDaResposta(
                "Área Territorial do Município de " + municipio,
                geograficosService.areaTerritorialMunicipal(municipio),
                "200"
        );
        return ResponseEntity.ok().body(respostaDaRequisicao);
    }


    @Operation(summary = "Busca a densidade demográfica do município",
            description = "Retorna o valor da densidade demográfica (em habitantes por km²) do município com base no nome do município informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Densidade demográfica encontrada e retornada com sucesso!",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Exemplo de requisição bem sucedida para a densidade demográfica do município de São Luís",
                                            value = """
                                                    {
                                                      "Status da Requisição": "200",
                                                      "Resposta da Requisição": "{\\"Densidade Demográfica\\" : 1779.87, \\"Referência dos Dados\\" : \\"2022\\", \\"Fonte dos Dados\\" : \\"SIDRA\\"}",
                                                      "Indicador da Requisição": "Densidade Demográfica do Município de São Luís"
                                                    }"""
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "204",
                    description = "Densidade demográfica não encontrada"),
            @ApiResponse(responseCode = "400",
                    description = "Parâmetro município não informado ou inválido"),
            @ApiResponse(responseCode = "404",
                    description = "Endpoint não encontrado"),
            @ApiResponse(responseCode = "500",
                    description = "Erro na requisição dos dados")
    })
    @GetMapping("/densidadeDemografica")
    public ResponseEntity<HashMap<String, Object>> BuscarDensidadeDemografica(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @Valid @RequestParam("municipio") String municipio){
        HashMap<String, Object> respostaDaRequisicao = resposta.CorpoDaResposta(
                "Densidade Demográfica do Município de " + municipio,
                geograficosService.densidadeDemograficaMunicipal(municipio),
                "200"
        );
        return ResponseEntity.ok().body(respostaDaRequisicao);
    }
}

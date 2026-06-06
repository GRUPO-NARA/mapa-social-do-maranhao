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
import mpma.mapa.service.InformacoesService;
import mpma.mapa.service.Resposta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/informacoes")
@RateLimiter(name = "RateLimiter")
@Tag(name = "Informações", description = "Endpoints para dados informativos")
public class InformacoesController {

    @Autowired
    private InformacoesService informacoesService;

    @Autowired
    private Resposta resposta;

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
            @Valid @RequestParam("municipio") String municipio){
        HashMap<String, Object> respostaDaRequisicao = resposta.CorpoDaResposta(
                "Dados Principais do município de " + municipio,
                informacoesService.principaisInformacoesMunicipal(municipio),
                "200"
        );
        return ResponseEntity.ok().body(respostaDaRequisicao);

    }
}

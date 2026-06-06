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
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Parâmetros de entrada inválidos\"}"))),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Dados de idade mediana não encontrados para o município e ano informados\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Ocorreu um erro ao processar a requisição\"}")))
    })
    @GetMapping("/idadeMediana")
    public ResponseEntity<HashMap<String, Object>> BuscarIdadeMediana(
            @Parameter(
                    name = "municipio",
                    description = "nome do município para o qual se deseja obter a idade mediana (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @Valid @RequestParam("municipio") String municipio
    ) {
        HashMap<String, Object> respostaDaRequisicao = resposta.CorpoDaResposta(
                "Idade Mediana do Município de " + municipio,
                saudeService.idadeMedianaMunicipal(municipio),
                "200"
        );
        return ResponseEntity.ok().body(respostaDaRequisicao);
    }
}
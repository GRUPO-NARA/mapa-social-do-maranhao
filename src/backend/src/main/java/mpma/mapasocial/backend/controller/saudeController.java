package mpma.mapasocial.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import mpma.mapasocial.backend.service.saude.saudeService;
import mpma.mapasocial.backend.service.RespostaRequisicao;

@RestController
@RequestMapping("/saude")
@Tag(name = "Saúde", description = "Endpoints para dados de saúde")
public class saudeController {

    @Autowired
    private RespostaRequisicao service;

    @Autowired
    private saudeService saudeService;

    @Operation(
            summary = "Busca a idade mediana por município",
            description = "Retorna o valor da idade mediana da população com base no ano e no município informado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Idade mediana encontrada com sucesso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"idadeMediana\": 35.5}"))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Parâmetros de entrada inválidos\"}"))),
            @ApiResponse(responseCode = "404", description = "Dados não encontrados", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Dados de idade mediana não encontrados para o município e ano informados\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Ocorreu um erro ao processar a requisição\"}")))
    })
    @GetMapping("/buscarIdadeMediana")
    public ResponseEntity<?> buscarIdadeMediana(
            @Parameter(
                    name = "ano",
                    description = "ano de referência da coleta dos dados(ex: 2022)",
                    example = "2022",
                    required = true
            )
            @RequestParam("ano") String ano,

            @Parameter(
                    name = "municipio",
                    description = "nome do município para o qual se deseja obter a idade mediana (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @RequestParam("municipio") String municipio
    ) {
        try {
            Double idadeMediana = saudeService.buscarIdadeMedianaPorMunicipio(ano, municipio);
            if (idadeMediana == null){
                return ResponseEntity.noContent().build();
            }
            var resposta = service.criarCorpo(
                    "200",
                    "idade mediana do municipio de " + municipio,
                    new RespostaRequisicao.ObjetoDeResposta(idadeMediana)
            );

            return ResponseEntity.ok(resposta);

        } catch (Exception e){
            // Correção aqui: Corrigido o parêntese do método criarCorpo
            var respostaErro = service.criarCorpo(
                    "500",
                    "Ocorreu um erro ao processar a requisição",
                    new RespostaRequisicao.ObjetoDeResposta(e.getMessage())
            );
            return ResponseEntity.internalServerError().body(respostaErro);
        }
    }
}
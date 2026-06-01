package mpma.mapasocial.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import mpma.mapasocial.backend.service.saude.saudeService;
import mpma.mapasocial.backend.service.RespostaRequisicao;

/**
 * Controller responsável por expor os endpoints de saúde do backend.
 *
 * Os métodos desta classe delegam a lógica de negócio ao serviço
 * {@link mpma.mapasocial.backend.service.saude.saudeService} e utilizam
 * {@link RespostaRequisicao} para construir respostas consistentes.
 */
@RestController
@RequestMapping("/saude")
@CrossOrigin("http://localhost:3000")
@Tag(name = "Saúde", description = "Endpoints para dados de saúde")
public class saudeController {

    @Autowired
    private RespostaRequisicao service;

    @Autowired
    private saudeService saudeService;

    /**
     * Endpoint que retorna a idade mediana da população para o município solicitado.
     *
     * O método chama {@link saudeService#buscarIdadeMedianaPorMunicipio(String, String)}
     * e retorna um corpo de resposta padronizado. Se não houver dados, responde com 204;
     * em caso de exceção, responde com 500 e a mensagem de erro.
     */
    @Operation(
            summary = "Busca a idade mediana por município",
            description = "Retorna o valor da idade mediana da população com base no município informado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Idade mediana encontrada com sucesso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"idadeMediana\": 35.5}"))),
            @ApiResponse(responseCode = "204", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Parâmetros de entrada inválidos\"}"))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Ocorreu um erro ao processar a requisição\"}")))
    })
    @GetMapping("/buscarIdadeMediana")
    public ResponseEntity<?> buscarIdadeMediana(
            @Parameter(
                    name = "municipio",
                    description = "nome do município para o qual se deseja obter a idade mediana (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @RequestParam("municipio") String municipio
    ) {
        try {
            Double idadeMediana = saudeService.buscarIdadeMedianaPorMunicipio(null, municipio);
            if (idadeMediana == null) {
                return ResponseEntity.noContent().build();
            }
            var resposta = service.criarCorpo(
                    "200",
                    "idade mediana do municipio de " + municipio,
                    new RespostaRequisicao.ObjetoDeResposta(idadeMediana)
            );

            return ResponseEntity.ok(resposta);

        } catch (Exception e) {
            var respostaErro = service.criarCorpo(
                    "500",
                    "Ocorreu um erro ao processar a requisição",
                    new RespostaRequisicao.ObjetoDeResposta(e.getMessage())
            );
            return ResponseEntity.internalServerError().body(respostaErro);
        }
    }
}

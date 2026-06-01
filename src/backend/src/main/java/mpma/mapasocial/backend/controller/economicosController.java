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
import org.springframework.web.bind.annotation.*;
import mpma.mapasocial.backend.service.RespostaRequisicao;
import mpma.mapasocial.backend.service.economicos.economicosService;

import java.util.HashMap;

/**
 * Controller de dados econômicos responsável por expor endpoints REST para
 * indicadores de Produto Interno Bruto no estado do Maranhão.
 *
 * As requisições são encaminhadas ao serviço econômico e retornam resultados
 * no formato padrão de resposta do projeto.
 */
@RestController
@RequestMapping("/economicos")
@CrossOrigin("http://localhost:3000")
@Tag(name = "Econômicos", description = "Endpoints para dados econômicos")
public class economicosController {
    @Autowired
    private economicosService economicosService;

    @Autowired
    private RespostaRequisicao RespostaRequisicao;

    /**
     * Busca o Produto Interno Bruto municipal para o município informado.
     *
     * O método repassa o nome do município para o serviço econômico e, se o
     * resultado estiver disponível, retorna o PIB encapsulado na resposta
     * padrão do projeto. Quando não houver dados, retorna 204.
     *
     * @param municipio nome do município para busca textual
     * @return ResponseEntity com o PIB municipal ou 204 quando não houver dados
     */
    @Operation(summary = "Busca o Produto Interno Bruto municipal",
    description = "Retorna o PIB do municípip informado")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto Interno Bruto encontrado e retornado com sucesso!",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Exemplo de requisição bem sucedida para o PIB do município de São Luís",
                                            value = "{\n" +
                                                    "  \"indicador\": \"Produto Interno Bruto do Município de São Luís\",\n" +
                                                    "  \"resposta\": {\n" +
                                                    "    \"valor\": 4.23890511E7\n" +
                                                    "  },\n" +
                                                    "  \"status\": \"200\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Não foram encontrados dados do município informado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parâmetro município não informado ou inválido",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Endpoint não encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro na requisição dos dados",
                    content = @Content
            )
    })
    @GetMapping("/buscarProdutoInternoBrutoMunicipal")
    public ResponseEntity<?> buscarProdutoInternoBruto(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @RequestParam("municipio") String municipio){
       HashMap<String, Object> pibMunicipal = economicosService.produtoInternoBruto(municipio);

        if (pibMunicipal == null){
            return ResponseEntity.noContent().build();
        }

        var resposta = RespostaRequisicao.criarCorpo(
                "200",
                "Produto Interno Bruto do Município de " + municipio,
                new RespostaRequisicao.ObjetoDeResposta(pibMunicipal)
        );
        return ResponseEntity.ok().body(resposta);
    }

    /**
     * Busca o Produto Interno Bruto agregado mais recente do estado do Maranhão.
     *
     * O endpoint consulta o serviço econômico e retorna o PIB estadual no formato
     * padrão do projeto. Caso o serviço não encontre resultado, retorna 204.
     *
     * @return ResponseEntity com o PIB agregado ou 204 quando não houver dados
     */
    @GetMapping("/buscarProdutoInternoBrutoAgregado")
    public ResponseEntity<?> buscarProdutoInternoBrutoAgregado(){
        HashMap<String, Object> pibAgregadoEstadual = economicosService.produtoInternoBrutoAgregadoRecente();

        if (pibAgregadoEstadual == null){
            return ResponseEntity.noContent().build();
        }

        var resposta = RespostaRequisicao.criarCorpo(
                "200",
                "Produto Interno Bruto Agregado do Estado do Maranhão",
                new RespostaRequisicao.ObjetoDeResposta(pibAgregadoEstadual)
        );

        return ResponseEntity.ok().body(resposta);
    }
}

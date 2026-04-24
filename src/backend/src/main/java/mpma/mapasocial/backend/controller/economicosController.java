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
import mpma.mapasocial.backend.repository.economicos.ProdutoInternoBrutoMunicipalRepository;
import mpma.mapasocial.backend.service.gerarRespostaRequisicaoService;
import mpma.mapasocial.backend.service.economicos.economicosService;
@RestController
@RequestMapping("/economicos")
@Tag(name = "Econômicos", description = "Endpoints para dados econômicos")
public class economicosController {
    @Autowired
    private economicosService economicosService;

    @Autowired
    private gerarRespostaRequisicaoService gerarRespostaRequisicaoService;

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
                                    ),
                                    @ExampleObject(
                                            name = "Exemplo de requisição bem sucedida para o PIB do município de Açailândia",
                                            value = "{\n" +
                                                    "  \"indicador\": \"Produto Interno Bruto do Município de Açailândia\",\n" +
                                                    "  \"resposta\": {\n" +
                                                    "    \"valor\": 4274161.961\n" +
                                                    "  },\n" +
                                                    "  \"status\": \"200\"\n" +
                                                    "}"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    description = "Não foram encontrados dados do município informado",
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
        Double pibMunicipal = economicosService.produtoInternoBrutoMunicipal(municipio);

        if (pibMunicipal == null){
            return ResponseEntity.noContent().build();
        }

        var resposta = gerarRespostaRequisicaoService.criarCorpo(
                "200",
                "Produto Interno Bruto do Município de " + municipio,
                new gerarRespostaRequisicaoService.RespostaDouble(pibMunicipal)
        );
        return ResponseEntity.ok().body(resposta);
    }
}

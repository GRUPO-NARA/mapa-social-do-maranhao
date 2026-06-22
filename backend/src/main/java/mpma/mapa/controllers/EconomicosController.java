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
import org.springframework.web.bind.annotation.*;
import mpma.mapa.service.Resposta;
import mpma.mapa.service.Economicos.EconomicosService;

import java.util.HashMap;

@RestController
@RequestMapping("/economicos")
@RateLimiter(name = "RateLimiter")
@Tag(name = "Econômicos", description = "Endpoints relacionados a dados econômicos dos municípios do Maranhão.")
public class EconomicosController {

    @Autowired
    private EconomicosService economicosService;

    @Autowired
    private Resposta resposta;

    @Operation(summary = "Busca o Produto Interno Bruto municipal",
            description = "Retorna o Produto Interno Bruto (PIB) do município especificado, com base nos dados mais recentes disponíveis. O PIB é um indicador econômico que representa a soma de todos os bens e serviços produzidos em um município durante um determinado período.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto Interno Bruto encontrado e retornado com sucesso!",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Exemplo de requisição bem sucedida para o PIB do município de São Luís",
                                            value = """
                                                    {
                                                      "Resposta da Requisição": "{\\"Produto Interno Bruto\\" : 42389051.1, \\"Referência dos Dados\\" : \\"2023\\", \\"Fonte dos Dados\\" : \\"SIDRA\\"}",
                                                      "Status da Requisição": "200",
                                                      "Indicador da Requisição": "Produto Interno Bruto do Município de São Luís"
                                                    }"""
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "204", description = "Não foram encontrados dados do município informado"),
            @ApiResponse(responseCode = "400", description = "Parâmetro município não informado ou inválido"),
            @ApiResponse(responseCode = "404", description = "Endpoint não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro na requisição dos dados")
    })
    @GetMapping("/produtoInternoBruto")
    public ResponseEntity<HashMap<String, Object>> BuscarProdutoInternoBrutoDoEstado(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @Valid @RequestParam("municipio") String municipio){
        HashMap<String, Object> respostaDaRequisicao = resposta.CorpoDaResposta(
                "Produto Interno Bruto do Município de " + municipio,
                economicosService.produtoInternoBrutoMunicipal(municipio),
                "200"
        );
        return ResponseEntity.ok().body(respostaDaRequisicao);
    }

    @Operation(summary = "Busca o Produto Interno Bruto agregado do estado do Maranhão",
            description = "Retorna o Produto Interno Bruto (PIB) agregado do estado do Maranhão, com base nos dados mais recentes disponíveis. O PIB agregado representa a soma de todos os bens e serviços produzidos em todo o estado durante um determinado período.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto Interno Bruto agregado encontrado e retornado com sucesso!",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Exemplo de requisição bem sucedida para o PIB agregado do estado do Maranhão",
                                            value = """
                                                    {
                                                      "Resposta da Requisição": "{\\"Produto Interno Bruto Agregado\\" : 120507.877, \\"Referência dos Dados\\" : \\"2023\\", \\"Fonte dos Dados\\" : \\"SIDRA\\"}",
                                                      "Status da Requisição": "200",
                                                      "Indicador da Requisição": "Produto Interno Bruto Agregado do Estado do Maranhão"
                                                    }"""
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "204", description = "Não foram encontrados dados para o estado do Maranhão"),
            @ApiResponse(responseCode = "404", description = "Endpoint não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro na requisição dos dados")
    })
    @GetMapping("/produtoInternoBrutoAgregadoEstadual")
    public ResponseEntity<HashMap<String, Object>> BuscarProdutoInternoBrutoAgregadoDoEstado(){
        HashMap<String, Object> respostaDaRequisicao = resposta.CorpoDaResposta(
                "Produto Interno Bruto Agregado do Estado do Maranhão",
                economicosService.produtoInternoBrutoAgregadoEstadualRecente(),
                "200"
        );
        return ResponseEntity.ok().body(respostaDaRequisicao);
    }

    @Operation(summary = "Busca o Produto Interno Bruto per capita municipal",
            description = "Retorna o Produto Interno Bruto (PIB) per capita do município especificado, dividindo o valor do PIB total pela população local. Representa uma média de quanto cada habitante produziria caso a riqueza fosse dividida igualmente.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "PIB per capita encontrado e retornado com sucesso!",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Exemplo de requisição bem sucedida para o PIB per capita do município de São Luís",
                                            value = """
                                                    {
                                                      "Resposta da Requisição": "{\\"PIB Per Capita\\" : 39250.42, \\"Referência dos Dados\\" : \\"2023\\", \\"Fonte dos Dados\\\" : \\"SIDRA\\"}",
                                                      "Status da Requisição": "200",
                                                      "Indicador da Requisição": "PIB Per Capita do Município de São Luís"
                                                    }"""
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "204", description = "Não foram encontrados dados do município informado"),
            @ApiResponse(responseCode = "400", description = "Parâmetro município não informado ou inválido"),
            @ApiResponse(responseCode = "404", description = "Endpoint não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro na requisição dos dados")
    })
    @GetMapping("/pibPerCapita")
    public ResponseEntity<HashMap<String, Object>> BuscarPibPerCapitaDoMunicipio(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @Valid @RequestParam("municipio") String municipio) {
        HashMap<String, Object> respostaDaRequisicao = resposta.CorpoDaResposta(
                "PIB Per Capita do Município de " + municipio,
                economicosService.produtoInternoBrutoPerCapitaMunicipal(municipio),
                "200"
        );
        return ResponseEntity.ok().body(respostaDaRequisicao);
    }
}
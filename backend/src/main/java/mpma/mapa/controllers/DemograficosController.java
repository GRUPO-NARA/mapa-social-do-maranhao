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
import mpma.mapa.service.Resposta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mpma.mapa.service.Demograficos.DemograficosService;
import java.util.HashMap;

@RestController
@RequestMapping("/demograficos")
@RateLimiter(name = "RateLimiter")
@Tag(name = "Demográficos", description = "Endpoints relacionados a dados demográficos dos municípios do Maranhão.")
public class DemograficosController {

    @Autowired
    private Resposta resposta;

    @Autowired
    private DemograficosService demograficosService;

    @Operation(summary = "Busca a população total do município",
            description = "Retorna o valor da população total do município com base no nome do município informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "População encontrada e retornada com sucesso!",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemplo de requisição bem sucedida para a população total do município de São Luís",
                                    value = "{\n" +
                                            "  \"Resposta da Requisição\": \"{\\\"Quantidade de Pessoas\\\" : 1037775, \\\"Referência dos Dados\\\" : \\\"2022\\\", \\\"Fonte dos Dados\\\" : \\\"SIDRA\\\"}\",\n" +
                                            "  \"Status da Requisição\": \"200\",\n" +
                                            "  \"Indicador da Requisição\": \"População Total do Município de São Luís\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "204",
                    description = "População não encontrada"),
            @ApiResponse(responseCode = "400",
                    description = "Parâmetro município não informado ou inválido!"),
            @ApiResponse(responseCode = "404",
                    description = "Endpoint não encontrado!"),
            @ApiResponse(responseCode = "500",
                    description = "Erro na requisição dos dados")
    })
    @GetMapping("/populacao")
    public ResponseEntity<HashMap<String, Object>> BuscarPopulacaoTotalDoMunicipio(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @Valid @RequestParam("municipio") String municipio
    ){
        HashMap<String, Object> respostaDaRequisicao = resposta.CorpoDaResposta(
                "População Total do Município de " + municipio,
                demograficosService.populacaoMunicipal(municipio),
                "200"
        );
        return ResponseEntity.ok().body(respostaDaRequisicao);
    }

    @Operation(summary = "Busca a população total do estado",
            description = "Retorna o valor da população total do estado com base no nome do estado informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "População encontrada e retornada com sucesso!",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemplo de requisição bem sucedida para a população total do estado do Maranhão",
                                    value = "{\n" +
                                            "  \"Resposta da Requisição\": \"{\\\"Quantidade de Pessoas\\\" : 6776699, \\\"Referência dos Dados\\\" : \\\"2022\\\", \\\"Fonte dos Dados\\\" : \\\"SIDRA\\\"}\",\n" +
                                            "  \"Status da Requisição\": \"200\",\n" +
                                            "  \"Indicador da Requisição\": \"População Total do Estado do Maranhão - Recente\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "204",
                    description = "População não encontrada"),
            @ApiResponse(responseCode = "400",
                    description = "Parâmetro estado não informado ou inválido!"),
            @ApiResponse(responseCode = "404",
                    description = "Endpoint não encontrado!"),
            @ApiResponse(responseCode = "500",
                    description = "Erro na requisição dos dados")
    })
    @GetMapping("/populacaoEstadualRecente")
    public ResponseEntity<HashMap<String, Object>> BuscarPopulacaoTotalDoEstado(){
        HashMap<String, Object> respostaDaRequisicao = resposta.CorpoDaResposta(
                "População Total do Estado do Maranhão - Recente",
                demograficosService.populacaoEstadualRecente(),
                "200"
        );
        return ResponseEntity.ok().body(respostaDaRequisicao);
    }

    @Operation(summary = "Busca a quantidade de homens total do município",
    description = "Retorna o valor da quantidade de homens total por município com base no nome do município informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Quantidade de homens encontrada e retornada com sucesso!",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemplo de requisição bem sucedida para a quantidade de homens do município de São Luís",
                                    value = "{\n" +
                                            "  \"Resposta da Requisição\": \"{\\\"Quantidade de Homens\\\" : 483501, \\\"Referência dos Dados\\\" : \\\"2022\\\", \\\"Fonte dos Dados\\\" : \\\"SIDRA\\\"}\",\n" +
                                            "  \"Status da Requisição\": \"200\",\n" +
                                            "  \"Indicador da Requisição\": \"Quantidade de Homens do Município de São Luís\"\n" +
                                            "}"
                            )
                    )),
            @ApiResponse(responseCode = "204",
                    description = "Não foi encontrado a quantidade de homens para o município informado!"),
            @ApiResponse(responseCode = "404",
                    description = "Endpoint não encontrado!"),
            @ApiResponse(responseCode = "400",
                    description = "Parâmetro município não informado ou inválido!"),
            @ApiResponse(responseCode = "500",
                    description = "Erro na requisição dos dados!")
    })
    @GetMapping("/quantidadeDeHomens")
    public ResponseEntity<HashMap<String, Object>> BuscarQuantidadeDeHomensDoMunicipio(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @Valid @RequestParam("municipio") String municipio
    ){
        HashMap<String, Object> respostaDaRequisicao = resposta.CorpoDaResposta(
                "Quantidade de Homens do Município de " + municipio,
                demograficosService.quantidadeDeHomensMunicipal(municipio),
                "200"
            );
        return ResponseEntity.ok().body(respostaDaRequisicao);
    }


    @Operation(summary = "Busca a quantidade de mulheres total do município",
    description = "Retorna o valor da quantidade de mulheres total do município com base no nome do município informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Quantidade de mulheres encontrada com sucesso!",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemplo de requisição bem sucedida para a quantidade de mulheres do município de São Luís",
                                    value = "{\n" +
                                            "  \"Resposta da Requisição\": \"{\\\"Quantidade de Mulheres\\\" : 554274, \\\"Referência dos Dados\\\" : \\\"2022\\\", \\\"Fonte dos Dados\\\" : \\\"SIDRA\\\"}\",\n" +
                                            "  \"Status da Requisição\": \"200\",\n" +
                                            "  \"Indicador da Requisição\": \"Quantidade de Mulheres do Município de São Luís\"\n" +
                                            "}"
                            )
                    )),
            @ApiResponse(responseCode = "204",
                    description = "Não foi encontrado a quantidade de mulheres para o município informado!"),
            @ApiResponse(responseCode = "400",
                    description = "Parâmetro município não informado ou inválido!"),
            @ApiResponse(responseCode = "404",
                    description = "Endpoint não encontrado!"),
            @ApiResponse(responseCode = "500",
                    description = "Erro na requisição dos dados!")
    })
    @GetMapping("/quantidadeDeMulheres")
    public ResponseEntity<HashMap<String, Object>> BuscarQuantidadeDeMulheresDoMunicipio(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @Valid @RequestParam("municipio") String municipio
    ){
        HashMap<String, Object> respostaDaRequisicao = resposta.CorpoDaResposta(
                "Quantidade de Mulheres do Município de " + municipio,
                demograficosService.quantidadeDeMulheresMunicipal(municipio),
                "200"
        );
        return ResponseEntity.ok().body(respostaDaRequisicao);
    }

    @Operation(summary = "Busca a quantidade de residentes rurais total do município",
    description = "Retorna o valor da quantidade de residentes rurais total do município com base no nome do município informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "Quantidade de residentes rurais encontrada com sucesso!",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Exemplo de requisição bem sucedida para a quantidade de residentes rurais do município de São Luís",
                            value = "{\n" +
                                    "  \"Resposta da Requisição\": \"{\\\"Quantidade de Residentes Rurais\\\" : 554274, \\\"Referência dos Dados\\\" : \\\"2022\\\", \\\"Fonte dos Dados\\\" : \\\"SIDRA\\\"}\",\n" +
                                    "  \"Status da Requisição\": \"200\",\n" +
                                    "  \"Indicador da Requisição\": \"Quantidade de Residentes Rurais do Município de São Luís\"\n" +
                                    "}"
                    )
            )),
            @ApiResponse(responseCode = "204",
                description = "Não foi encontrado a quantidade de residentes rurais para o município informado!"),
            @ApiResponse(responseCode = "400",
                description = "Parâmetro município não informado ou inválido!"),
            @ApiResponse(responseCode = "404",
                description = "Endpoint não encontrado!"),
            @ApiResponse(responseCode = "500",
                description = "Erro na requisição dos dados!")
    })
    @GetMapping("/quantidadeDeResidentesRurais")
    public ResponseEntity<HashMap<String, Object>> BuscarQuantidadeDeResidentesRuraisDoMunicipio(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @Valid @RequestParam("municipio") String municipio
    ){
        HashMap<String, Object> respostaDaRequisicao = resposta.CorpoDaResposta(
                "Quantidade de Residentes Rurais do Município de " + municipio,
                demograficosService.quantidadeDeResidentesRuraisMunicipal(municipio),
                "200"
        );
        return ResponseEntity.ok().body(respostaDaRequisicao);
    }

    @Operation(summary = "Busca o Índice de Desenvolvimento Humano do município",
    description = "Retorna o valor do Índice de Desenvolvimento Humano do município com base no nome do município informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "Índice de Desenvolvimento Humano encontrado com sucesso!",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Exemplo de requisição bem sucedida para o Índice de Desenvolvimento Humano do município de São Luís",
                            value = "{\n" +
                                    "  \"Resposta da Requisição\": \"{\\\"Índice de Desenvolvimento Humano\\\" : 0.768, \\\"Referência dos Dados\\\" : 2010, \\\"Fonte dos Dados\\\" : \\\"IPEADATA\\\"}\",\n" +
                                    "  \"Status da Requisição\": \"200\",\n" +
                                    "  \"Indicador da Requisição\": \"Índice de Desenvolvimento Humano do Município de São Luís\"\n" +
                                    "}"
                    )
            )
            ),
            @ApiResponse(responseCode = "204",
            description = "Não foi encontrado o Índice de Desenvolvimento Humano para o município informado!"),
            @ApiResponse(responseCode = "400",
            description = "Parâmetro município não informado ou inválido!"),
            @ApiResponse(responseCode = "404",
            description = "Endpoint não encontrado!"),
            @ApiResponse(responseCode = "500",
            description = "Erro na requisição dos dados!")
    })
    @GetMapping("/idh")
    public ResponseEntity<HashMap<String, Object>> BuscarIndiceDeDesenvolvimentoHumanoDoMunicipio(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @Valid @RequestParam("municipio") String municipio){
        HashMap<String, Object> respostaDaRequisicao = resposta.CorpoDaResposta(
                "Índice de Desenvolvimento Humano do Município de " + municipio,
                demograficosService.indiceDeDesenvolvimentoHumanoMunicipal(municipio),
                "200"
        );
        return ResponseEntity.ok().body(respostaDaRequisicao);
    }

    @Operation(summary = "Busca a evolução do Índice de Desenvolvimento Humano do município",
    description = "Retorna o valor da evolução do Índice de Desenvolvimento Humano do município com base no nome do município informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "Evolução do IDH encontrada e retornada com sucesso!",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            value = "{\n" +
                                    "  \"Resposta da Requisição\": [\n" +
                                    "    \"{\\\"Referência\\\" : 1991, \\\"Valor do IDH\\\" : 0.562}\",\n" +
                                    "    \"{\\\"Referência\\\" : 2000, \\\"Valor do IDH\\\" : 0.658}\",\n" +
                                    "    \"{\\\"Referência\\\" : 2010, \\\"Valor do IDH\\\" : 0.768}\"\n" +
                                    "  ],\n" +
                                    "  \"Status da Requisição\": \"200\",\n" +
                                    "  \"Indicador da Requisição\": \"Evolução do Índice de Desenvolvimento Humano do Município de São Luís\"\n" +
                                    "}\n"
                    )
            )
            ),
            @ApiResponse(responseCode = "204",
                description = "Não foi encontrado a evolução do Índice de Desenvolvimento Humano para o município informado!"),
            @ApiResponse(responseCode = "400",
                description = "Parâmetro município não informado ou inválido!"),
            @ApiResponse(responseCode = "404",
                description = "Endpoint não encontrado!"),
            @ApiResponse(responseCode = "500",
                description = "Erro na requisição dos dados!")
    })
    @GetMapping("/evolucaoIDH")
    public ResponseEntity<HashMap<String, Object>> BuscarEvolucaoIndiceDeDesenvolvimentoHumanoDoMunicipio(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @Valid @RequestParam("municipio") String municipio
    ) {
        HashMap<String, Object> respostaDaRequisicao = resposta.CorpoDaResposta(
                "Evolução do Índice de Desenvolvimento Humano do Município de " + municipio,
                demograficosService.evolucaoDoIndiceDeDesenvolvimentoHumanoMunicipal(municipio),
                "200"
        );
        return ResponseEntity.ok().body(respostaDaRequisicao);
    }





}

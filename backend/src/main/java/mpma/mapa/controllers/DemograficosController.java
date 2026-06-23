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
            @ApiResponse(responseCode = "200", description = "População encontrada e retornada com sucesso!", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Exemplo de requisição bem sucedida para a população total do município de São Luís", value = "{\n  \"Resposta da Requisição\": \"{\\\"Quantidade de Pessoas\\\" : 1037775, \\\"Referência dos Dados\\\" : \\\"2022\\\", \\\"Fonte dos Dados\\\" : \\\"SIDRA\\\"}\",\n  \"Status da Requisição\": \"200\",\n  \"Indicador da Requisição\": \"População Total do Município de São Luís\"\n}"))),
            @ApiResponse(responseCode = "400", description = "Parâmetro município inválido!"),
            @ApiResponse(responseCode = "500", description = "Erro na requisição dos dados")
    })
    @GetMapping("/populacao")
    public ResponseEntity<HashMap<String, Object>> BuscarPopulacaoTotalDoMunicipio(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ){
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("População Total do Município de " + municipio, demograficosService.populacaoMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca a população total do estado",
            description = "Retorna o valor da população total recente do estado do Maranhão")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "População encontrada com sucesso!", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Exemplo de requisição bem sucedida para a população total do estado do Maranhão", value = "{\n  \"Resposta da Requisição\": \"{\\\"Quantidade de Pessoas\\\" : 6776699, \\\"Referência dos Dados\\\" : \\\"2022\\\", \\\"Fonte dos Dados\\\" : \\\"SIDRA\\\"}\",\n  \"Status da Requisição\": \"200\",\n  \"Indicador da Requisição\": \"População Total do Estado do Maranhão - Recente\"\n}")))
    })
    @GetMapping("/populacaoEstadualRecente")
    public ResponseEntity<HashMap<String, Object>> BuscarPopulacaoTotalDoEstado(){
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("População Total do Estado do Maranhão - Recente", demograficosService.populacaoEstadualRecente(), "200"));
    }

    @Operation(summary = "Busca a quantidade de homens total do município")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantidade de homens retornada com sucesso!", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Exemplo de homens", value = "{\n  \"Resposta da Requisição\": \"{\\\"Quantidade de Homens\\\" : 483501, \\\"Referência dos Dados\\\" : \\\"2022\\\", \\\"Fonte dos Dados\\\" : \\\"SIDRA\\\"}\",\n  \"Status da Requisição\": \"200\",\n  \"Indicador da Requisição\": \"Quantidade de Homens do Município de São Luís\"\n}")))
    })
    @GetMapping("/quantidadeDeHomens")
    public ResponseEntity<HashMap<String, Object>> BuscarQuantidadeDeHomensDoMunicipio(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ){
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Quantidade de Homens do Município de " + municipio, demograficosService.quantidadeDeHomensMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca a quantidade de mulheres total do município")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantidade de mulheres encontrada com sucesso!", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Exemplo mulheres", value = "{\n  \"Resposta da Requisição\": \"{\\\"Quantidade de Mulheres\\\" : 554274, \\\"Referência dos Dados\\\" : \\\"2022\\\", \\\"Fonte dos Dados\\\" : \\\"SIDRA\\\"}\",\n  \"Status da Requisição\": \"200\",\n  \"Indicador da Requisição\": \"Quantidade de Mulheres do Município de São Luís\"\n}")))
    })
    @GetMapping("/quantidadeDeMulheres")
    public ResponseEntity<HashMap<String, Object>> BuscarQuantidadeDeMulheresDoMunicipio(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ){
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Quantidade de Mulheres do Município de " + municipio, demograficosService.quantidadeDeMulheresMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca a quantidade de residentes rurais total do município")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantidade encontrada com sucesso!", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Exemplo rurais", value = "{\n  \"Resposta da Requisição\": \"{\\\"Quantidade de Residentes Rurais\\\" : 23451, \\\"Referência dos Dados\\\" : \\\"2022\\\", \\\"Fonte dos Dados\\\" : \\\"SIDRA\\\"}\",\n  \"Status da Requisição\": \"200\",\n  \"Indicador da Requisição\": \"Quantidade de Residentes Rurais do Município de São Luís\"\n}")))
    })
    @GetMapping("/quantidadeDeResidentesRurais")
    public ResponseEntity<HashMap<String, Object>> BuscarQuantidadeDeResidentesRuraisDoMunicipio(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ){
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Quantidade de Residentes Rurais do Município de " + municipio, demograficosService.quantidadeDeResidentesRuraisMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca o Índice de Desenvolvimento Humano do município")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Índice encontrado com sucesso!", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Exemplo IDH", value = "{\n  \"Resposta da Requisição\": \"{\\\"Índice de Desenvolvimento Humano\\\" : 0.768, \\\"Referência dos Dados\\\" : 2010, \\\"Fonte dos Dados\\\" : \\\"IPEADATA\\\"}\",\n  \"Status da Requisição\": \"200\",\n  \"Indicador da Requisição\": \"Índice de Desenvolvimento Humano do Município de São Luís\"\n}")))
    })
    @GetMapping("/idh")
    public ResponseEntity<HashMap<String, Object>> BuscarIndiceDeDesenvolvimentoHumanoDoMunicipio(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ){
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Índice de Desenvolvimento Humano do Município de " + municipio, demograficosService.indiceDeDesenvolvimentoHumanoMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca a evolução do Índice de Desenvolvimento Humano do município")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evolução do IDH retornada com sucesso!", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n  \"Resposta da Requisição\": [\n    \"{\\\"Referência\\\" : 1991, \\\"Valor do IDH\\\" : 0.562}\",\n    \"{\\\"Referência\\\" : 2000, \\\"Valor do IDH\\\" : 0.658}\",\n    \"{\\\"Referência\\\" : 2010, \\\"Valor do IDH\\\" : 0.768}\"\n  ],\n  \"Status da Requisição\": \"200\",\n  \"Indicador da Requisição\": \"Evolução do Índice de Desenvolvimento Humano do Município de São Luís\"\n}")))
    })
    @GetMapping("/evolucaoIDH")
    public ResponseEntity<HashMap<String, Object>> BuscarEvolucaoIndiceDeDesenvolvimentoHumanoDoMunicipio(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ) {
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Evolução do Índice de Desenvolvimento Humano do Município de " + municipio, demograficosService.evolucaoDoIndiceDeDesenvolvimentoHumanoMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca a população residente em favelas do município")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "População em favelas encontrada!", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Exemplo Favela", value = "{\n  \"Resposta da Requisição\": \"{\\\"Quantidade de Pessoas em Favelas\\\" : 12345, \\\"Referência dos Dados\\\" : \\\"2022\\\", \\\"Fonte dos Dados\\\" : \\\"SIDRA\\\"}\",\n  \"Status da Requisição\": \"200\",\n  \"Indicador da Requisição\": \"População Residente em Favelas do Município de São Luís\"\n}")))
    })
    @GetMapping("/populacaoEmFavela")
    public ResponseEntity<HashMap<String, Object>> BuscarPopulacaoResidenteEmFavelaDoMunicipio(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ){
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("População Residente em Favelas do Município de " + municipio, demograficosService.populacaoResidenteEmFavelaMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca a evolução da população residente em favelas do município")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evolução encontrada!", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n  \"Resposta da Requisição\": [\n    \"{\\\"Referência\\\" : 2010, \\\"Quantidade de Pessoas em Favelas\\\" : 8000}\",\n    \"{\\\"Referência\\\" : 2022, \\\"Quantidade de Pessoas em Favelas\\\" : 12345}\"\n  ],\n  \"Status da Requisição\": \"200\",\n  \"Indicador da Requisição\": \"Evolução da População Residente em Favelas do Município de São Luís\"\n}")))
    })
    @GetMapping("/evolucaoPopulacaoEmFavela")
    public ResponseEntity<HashMap<String, Object>> BuscarEvolucaoPopulacaoResidenteEmFavelaDoMunicipio(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ) {
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Evolução da População Residente em Favelas do Município de " + municipio, demograficosService.evolucaoPopulacaoResidenteEmFavelaMunicipal(municipio), "200"));
    }

    @Operation(summary = "Busca a população residente em favelas do município por referência (ano)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "População por referência encontrada!", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Exemplo Favela Ano", value = "{\n  \"Resposta da Requisição\": \"{\\\"Quantidade de Pessoas em Favelas\\\" : 12345, \\\"Referência dos Dados\\\" : \\\"2022\\\", \\\"Fonte dos Dados\\\" : \\\"SIDRA\\\"}\",\n  \"Status da Requisição\": \"200\",\n  \"Indicador da Requisição\": \"População Residente em Favelas do Município de São Luís - 2022\"\n}")))
    })
    @GetMapping("/populacaoEmFavelaPorAno")
    public ResponseEntity<HashMap<String, Object>> BuscarPopulacaoResidenteEmFavelaDoMunicipioPorAno(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio,
            @Parameter(name = "referencia", description = "referência/ano (ex: 2022)", example = "2022", required = true)
            @Valid @RequestParam("referencia") String referencia
    ){
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("População Residente em Favelas do Município de " + municipio + " - Referência " + referencia, demograficosService.populacaoResidenteEmFavelaMunicipalPorAno(municipio, referencia), "200"));
    }

    @Operation(summary = "Busca a quantidade de residentes urbanos total do município",
            description = "Retorna o valor consolidado da população urbana para o município informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantidade de residentes urbanos retornada com sucesso!", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Exemplo urbanos", value = "{\n  \"Resposta da Requisição\": \"{\\\"Quantidade de Residentes Urbanos\\\" : 912450, \\\"Referência dos Dados\\\" : \\\"2022\\\", \\\"Fonte dos Dados\\\" : \\\"SIDRA\\\"}\",\n  \"Status da Requisição\": \"200\",\n  \"Indicador da Requisição\": \"Quantidade de Residentes Urbanos do Município de São Luís\"\n}"))),
            @ApiResponse(responseCode = "400", description = "Parâmetro município inválido!"),
            @ApiResponse(responseCode = "500", description = "Erro na requisição dos dados!")
    })
    @GetMapping("/quantidadeDeResidentesUrbano")
    public ResponseEntity<HashMap<String, Object>> BuscarQuantidadeDeResidentesUrbanoDoMunicipio(
            @Parameter(name = "municipio", description = "nome do município (ex: São Luís)", example = "São Luís", required = true)
            @Valid @RequestParam("municipio") String municipio
    ){
        return ResponseEntity.ok().body(resposta.CorpoDaResposta("Quantidade de Residentes Urbanos do Município de " + municipio, demograficosService.quantidadeDeResidentesUrbanoMunicipal(municipio), "200"));
    }
}
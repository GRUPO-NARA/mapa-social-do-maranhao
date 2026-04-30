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
import  mpma.mapasocial.backend.service.gerarRespostaRequisicaoService;
import mpma.mapasocial.backend.service.demograficos.demograficosService;
@RestController
@RequestMapping("/demograficos")
@Tag(name = "Demográficos", description = "Endpoints para dados demográficos")
public class demograficosController {

    @Autowired
    private gerarRespostaRequisicaoService service;

    @Autowired
    private demograficosService demograficosService;

    @Operation(summary = "Busca a população total por município",
    description = "Retorna o valor da população residente com base no nome do município informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "População encontrada e retornada com sucesso!",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemplo de requisição bem sucedida para a população total do município de São Luís",
                                    value = "{\n" +
                                            "  \"indicador\": \"População Total do Município de São Luís\",\n" +
                                            "  \"resposta\": {\n" +
                                            "    \"valor\": 1037775\n" +
                                            "  },\n" +
                                            "  \"status\": \"200\"\n" +
                                            "}"
                            )
                    )
            ),
            @ApiResponse(responseCode = "204", description = "População não encontrada"),
            @ApiResponse(responseCode = "400", description = "Parâmetro município não informado ou inválido!"),
            @ApiResponse(responseCode = "404", description = "Endpoint não encontrado!"),
            @ApiResponse(responseCode = "500", description = "Erro na requisição dos dados")
    })
    @GetMapping("/buscarPopulacaoTotal")
    public ResponseEntity<?> buscarPopulacaoTotal(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @RequestParam("municipio") String municipio
    ){
        Long populacaoTotalDoMunicipio = demograficosService.populacaoTotalDoMunicipio(municipio);

        if (populacaoTotalDoMunicipio == null){
            return ResponseEntity.noContent().build();
        }

        var resposta = service.criarCorpo(
                "200",
                "População Total do Município de " + municipio,
                new gerarRespostaRequisicaoService.RespostaLong(populacaoTotalDoMunicipio)
        );

        return ResponseEntity.ok().body(resposta);
    }

    @Operation(summary = "Busca a quantidade de homens total por município",
    description = "Retorna a quantidade de homens total por município informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Quantidade de homens encontrada com sucesso!",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemplo de requisição bem sucedida para a quantidade de homens do município de São Luís",
                                    value = "{\n" +
                                            "  \"indicador\": \"Quantidade de Homens do Município de Açailândia\",\n" +
                                            "  \"resposta\": {\n" +
                                            "    \"valor\": 52604\n" +
                                            "  },\n" +
                                            "  \"status\": \"200\"\n" +
                                            "}"
                            )
                    )),
            @ApiResponse(responseCode = "204", description = "Não foi encontrado a quantidade de homens para o município informado!"),
            @ApiResponse(responseCode = "404", description = "Endpoint não encontrado!"),
            @ApiResponse(responseCode = "400", description = "Parâmetro município não informado ou inválido!"),
            @ApiResponse(responseCode = "500", description = "Erro na requisição dos dados!")
    })
    @GetMapping("/buscarQuantidadeDeHomens")
    public ResponseEntity<?> buscarQuantidadeDeHomens(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @RequestParam("municipio") String municipio
    ){
        Long quantidadeDeHomensDoMunicipio = demograficosService.quantidadeDeHomensDoMunicipio(municipio);

        if (quantidadeDeHomensDoMunicipio == null){
            return ResponseEntity.noContent().build();
        }

        var resposta = service.criarCorpo(
                "200",
                "Quantidade de Homens do Município de " + municipio,
                new gerarRespostaRequisicaoService.RespostaLong(quantidadeDeHomensDoMunicipio)
        );

        return ResponseEntity.ok().body(resposta);
    }

    @Operation(summary = "Busca a quantidade de mulheres total por município",
    description = "Retorna a quantidade de mulheres total por município informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Quantidade de mulheres encontrada com sucesso!",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemplo de requisição bem sucedida para a quantidade de mulheres do município de São Luís",
                                    value = "{\n" +
                                            "  \"indicador\": \"Quantidade de Mulheres do Município de São Luís\",\n" +
                                            "  \"resposta\": {\n" +
                                            "    \"valor\": 554274\n" +
                                            "  },\n" +
                                            "  \"status\": \"200\"\n" +
                                            "}"
                            )
                    )),
            @ApiResponse(responseCode = "204", description = "Não foi encontrado a quantidade de mulheres para o município informado!"),
            @ApiResponse(responseCode = "400", description = "Parâmetro município não informado ou inválido!"),
            @ApiResponse(responseCode = "404", description = "Endpoint não encontrado!"),
            @ApiResponse(responseCode = "500", description = "Erro na requisição dos dados!")
    })
    @GetMapping("/buscarQuantidadeDeMulheres")
    public ResponseEntity<?> buscarQuantidadeDeMulheres(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @RequestParam("municipio") String municipio
    ){
        Long quantidadeDeMulheresDoMunicipio = demograficosService.quantidadeDeMulheresDoMunicipio(municipio);

        if (quantidadeDeMulheresDoMunicipio == null){
            return ResponseEntity.noContent().build();
        }

        var resposta = service.criarCorpo(
                "200",
                "Quantidade de Mulheres do Município de " + municipio,
                new gerarRespostaRequisicaoService.RespostaLong(quantidadeDeMulheresDoMunicipio)
        );

        return ResponseEntity.ok().body(resposta);
    }

    @Operation(summary = "Busca a quantidade de residentes rurais total por município",
    description = "Retorna a quantidade de residentes rurais total por município informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "Quantidade de residentes rurais encontrada com sucesso!",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Exemplo de requisição bem sucedida para a quantidade de residentes rurais do município de São Luís",
                            value = "{\n" +
                                    "  \"indicador\": \"Quantidade de Residentes Rurais do Município de São Luís\",\n" +
                                    "  \"resposta\": {\n" +
                                    "    \"valor\": 7836\n" +
                                    "  },\n" +
                                    "  \"status\": \"200\"\n" +
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
    @GetMapping("/buscarQuantidadeDeResidentesRurais")
    public ResponseEntity<?> buscarQuantidadeDeResidentesRurais(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @RequestParam("municipio") String municipio
    ){
        Long quantidadeDeResidentesRuraisDoMunicipio = demograficosService.quantidadeDeResidentesRuraisDoMunicipio(municipio);

        if (quantidadeDeResidentesRuraisDoMunicipio == null){
            return ResponseEntity.noContent().build();
        }

        var resposta = service.criarCorpo(
                "200",
                "Quantidade de Residentes Rurais do Município de " + municipio,
                new gerarRespostaRequisicaoService.RespostaLong(quantidadeDeResidentesRuraisDoMunicipio)
        );

        return ResponseEntity.ok().body(resposta);
    }

    @Operation(summary = "Busca o índice de desenvolvimento humano por município",
    description = "Retorna o valor do índice de desenvolvimento humano do município com base no ano da coleta dos dados e do município informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "Índice de desenvolvimento humano encontrado com sucesso!",
            content = @Content(
                    examples = {
                            @ExampleObject(
                                    name = "Exemplo de requisição bem sucedida para o índice de desenvolvimento humano do município de São Luís no ano de 2010",
                                    value = "{\n" +
                                            "  \"indicador\": \"Índice de Desenvolvimento Humano do Município de São Luís\",\n" +
                                            "  \"resposta\": {\n" +
                                            "    \"valor\": 0.768\n" +
                                            "  },\n" +
                                            "  \"status\": \"200\"\n" +
                                            "}"
                            )
                    }
            )),
            @ApiResponse(responseCode = "204",
            description = "Não foi encontrado dados do índice de desenvolvimento humano para o município e ano informado!"),
            @ApiResponse(responseCode = "400",
            description = "Parâmetro município ou ano não informado ou inválido!"),
            @ApiResponse(responseCode = "404",
            description = "Endpoint não encontrado!"),
            @ApiResponse(responseCode = "500",
            description = "Erro na requisição dos dados!")
    })
    @GetMapping("/buscarIndiceDeDesenvolvimentoHumano")
    private ResponseEntity<?> buscarIndiceDeDesenvolvimentoHumano(
            @Parameter(
                    name = "ano",
                    description = "ano de referência da coleta dos dados (ex: 2010)",
                    example = "2010",
                    required = true
            )
            @RequestParam("ano") Integer ano,
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @RequestParam("municipio") String municipio
    ){
        Double indiceDeDesenvolvimentoHumanoDoMunicipio = demograficosService.indiceDeDesenvolvimentoHumanoDoMunicipio(ano, municipio);

        if (indiceDeDesenvolvimentoHumanoDoMunicipio == null){
            return ResponseEntity.noContent().build();
        }

        var resposta = service.criarCorpo(
                "200",
                "Índice de Desenvolvimento Humano do Município de " + municipio,
                new gerarRespostaRequisicaoService.RespostaDouble(indiceDeDesenvolvimentoHumanoDoMunicipio)
        );

        return ResponseEntity.ok().body(resposta);
    }
}

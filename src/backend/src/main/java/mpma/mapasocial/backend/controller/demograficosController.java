package mpma.mapasocial.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import mpma.mapasocial.backend.service.RespostaRequisicao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mpma.mapasocial.backend.service.demograficos.demograficosService;

import java.util.HashMap;

@RestController
@RequestMapping("/demograficos")
@CrossOrigin("http://localhost:3000")
@Tag(name = "Demográficos", description = "Endpoints para dados demográficos")
public class demograficosController {

    @Autowired
    private RespostaRequisicao service;

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
                new RespostaRequisicao.ObjetoDeResposta(populacaoTotalDoMunicipio)
        );

        return ResponseEntity.ok().body(resposta);
    }

    @GetMapping("/buscarPopulacaoTotalDoEstado")
    public ResponseEntity<?> buscarPopulacaoTotalDoEstado(){
        HashMap<String, Object> populacaoTotalDoEstado = demograficosService.populacaoTotalAtualizadaDoEstado();

        if (populacaoTotalDoEstado == null){
            return ResponseEntity.noContent().build();
        }

        var resposta = service.criarCorpo(
                "200",
                "População Total do Estado",
                new RespostaRequisicao.ObjetoDeResposta(populacaoTotalDoEstado)
        );

        return ResponseEntity.ok(resposta);
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
                new RespostaRequisicao.ObjetoDeResposta(quantidadeDeHomensDoMunicipio)
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
                new RespostaRequisicao.ObjetoDeResposta(quantidadeDeMulheresDoMunicipio)
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
                new RespostaRequisicao.ObjetoDeResposta(quantidadeDeResidentesRuraisDoMunicipio)
        );

        return ResponseEntity.ok().body(resposta);
    }

    @GetMapping("/buscarIndiceDeDesenvolvimentoHumano")
    public ResponseEntity<?> buscarIndiceDeDesenvolvimentoHumano(@RequestParam("municipio") String municipio){
        HashMap<String, Object> idhMunicipal = demograficosService.indiceDeDesenvolvimentoHumanoDoMunicipio(municipio);

        if (idhMunicipal == null){
            return ResponseEntity.noContent().build();
        }

        var resposta = service.criarCorpo(
                "200",
                "Índice de Desenvolvimento Humano do Município de " + municipio,
                new RespostaRequisicao.ObjetoDeResposta(idhMunicipal)
        );

        return ResponseEntity.ok().body(resposta);
    }

    @GetMapping("/buscarEvolucaoIndiceDeDesenvolvimentoHumanoPorMunicipio")
    public ResponseEntity<?> buscarEvolucaoIndiceDeDesenvolvimentoHumanoPorMunicipio(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @RequestParam("municipio") String municipio
    ) {
        HashMap<String, Object> evolucaoIndiceDeDesenvolvimentoHumanoPorMunicipio = demograficosService.evolucaoIndiceDeDesenvolvimentoHumanoDoMunicipio(municipio);

        if (evolucaoIndiceDeDesenvolvimentoHumanoPorMunicipio == null) {
            return ResponseEntity.noContent().build();
        }

        var resposta = service.criarCorpo(
                "200",
                "Evolução do Índice de Desenvolvimento Humano do Município de " + municipio,
                new RespostaRequisicao.ObjetoDeResposta(evolucaoIndiceDeDesenvolvimentoHumanoPorMunicipio)
        );

        return ResponseEntity.ok().body(resposta);
    }
}

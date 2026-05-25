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
import mpma.mapasocial.backend.service.RespostaRequisicao;
import mpma.mapasocial.backend.service.geograficos.geograficosService;

@RestController
@RequestMapping("/geograficos")
@Tag(name = "Geográficos", description = "Endpoints para dados geográficos")
public class geograficosController {
    @Autowired
    private geograficosService geograficosService;

    @Autowired
    private RespostaRequisicao service;

    @Operation(summary = "Busca a área total do município",
            description = "Retorna o valor da área total (em km²) do município com base no nome do município informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "Área total encontrada e retornada com sucesso!",
            content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(
                                    name = "Exemplo de requisição bem sucedida para a área total do município de São Luís",
                                    value = "{\n" +
                                            "  \"indicador\": \"Área Total do Município de São Luís\",\n" +
                                            "  \"resposta\": {\n" +
                                            "    \"valor\": 583.063\n" +
                                            "  },\n" +
                                            "  \"status\": \"200\"\n" +
                                            "}"
                            )
                    }
            )),
            @ApiResponse(responseCode = "204",
            description = "Área total não encontrada",
            content = @Content),
            @ApiResponse(responseCode = "400",
            description = "Parâmetro município não informado ou inválido",
            content = @Content),
            @ApiResponse(responseCode = "404",
            description = "Endpoint não encontrado",
            content = @Content),
            @ApiResponse(responseCode = "500",
            description = "Erro na requisição dos dados",
            content = @Content)
    })
    @GetMapping("/buscarAreaTotal")
    private ResponseEntity<?> buscaAreaTotal(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @RequestParam("municipio") String municipio){
        Double areaTotal = geograficosService.areaTerritorialDoMunicipio(municipio);

        if (areaTotal == null){
            return ResponseEntity.noContent().build();
        }

        var resposta = service.criarCorpo(
                "200",
                "Área Total do Município de " + municipio,
                new RespostaRequisicao.ObjetoDeResposta(areaTotal)
        );

        return ResponseEntity.ok().body(resposta);
    }

    @Operation(summary = "Busca a densidade demográfica do município",
            description = "Retorna o valor da densidade demográfica (em habitantes por km²) do município com base no nome do município informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Densidade demográfica encontrada e retornada com sucesso!",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Exemplo de requisição bem sucedida para a densidade demográfica do município de São Luís",
                                            value = "{\n" +
                                                    "  \"indicador\": \"Densidade Demográfica do Município de São Luís\",\n" +
                                                    "  \"resposta\": {\n" +
                                                    "    \"valor\": 1778.0\n" +
                                                    "  },\n" +
                                                    "  \"status\": \"200\"\n" +
                                                    "}"
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "204",
                    description = "Densidade demográfica não encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Parâmetro município não informado ou inválido",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Endpoint não encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Erro na requisição dos dados",
                    content = @Content)
    })
    @GetMapping("/buscarDensidadeDemografica")
    public ResponseEntity<?> buscarDensidadeDemografica(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @RequestParam("municipio") String municipio){
        Double densidadeDemografica = geograficosService.densidadeDemograficaDoMunicipio(municipio);

        if (densidadeDemografica == null){
            return ResponseEntity.noContent().build();
        }

        var resposta = service.criarCorpo(
                "200",
                "Densidade Demográfica do Município de " + municipio,
                new RespostaRequisicao.ObjetoDeResposta(densidadeDemografica)
        );

        return ResponseEntity.ok().body(resposta);
    }
}

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
import mpma.mapasocial.backend.repository.demograficos.PopulacaoResidenteRepository;
import mpma.mapasocial.backend.repository.demograficos.QuantidadeDeHomensRepository;
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
            @ApiResponse(responseCode = "204",
                    description = "População não encontrada",
                    content = @Content()
            )
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
            @ApiResponse(responseCode = "200", description = "Quantidade de homens encontrada com sucesso!"),
            @ApiResponse(responseCode = "204", description = "Não foi encontrado a quantidade de homens para o município informado!"),
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

}

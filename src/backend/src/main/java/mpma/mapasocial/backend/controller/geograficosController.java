package mpma.mapasocial.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import mpma.mapasocial.backend.service.gerarRespostaRequisicaoService;
import mpma.mapasocial.backend.service.geograficos.geograficosService;

@RestController
@RequestMapping("/geograficos")
@Tag(name = "Geográficos", description = "Endpoints para dados geográficos")
public class geograficosController {
    @Autowired
    private geograficosService geograficosService;

    @Autowired
    private gerarRespostaRequisicaoService service;

    @Operation(summary = "Busca a área total do município",
            description = "Retorna o valor da área total do município com base no nome do município informado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "Área total encontrada e retornada com sucesso!"),
            @ApiResponse(responseCode = "204",
            description = "Área total não encontrada"),
            @ApiResponse(responseCode = "400",
            description = "Parâmetro município não informado ou inválido"),
            @ApiResponse(responseCode = "404",
            description = "Endpoint não encontrado"),
            @ApiResponse(responseCode = "500",
            description = "Erro na requisição dos dados")
    })
    @GetMapping("/buscarAreaTotalPorMunicipio")
    private ResponseEntity<?> buscaAreaTotalPorMunicipio(
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
                new gerarRespostaRequisicaoService.RespostaDouble(areaTotal)
        );

        return ResponseEntity.ok().body(resposta);
    }
}

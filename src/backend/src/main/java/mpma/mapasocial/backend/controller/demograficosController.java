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

/**
 * Controller de dados demográficos que expõe endpoints REST para indicadores
 * populacionais do Maranhão.
 *
 * As requisições são encaminhadas para o serviço de demográficos e retornam
 * respostas no formato padrão de resposta do projeto.
 */
@RestController
@RequestMapping("/demograficos")
@CrossOrigin("http://localhost:3000")
@Tag(name = "Demográficos", description = "Endpoints para dados demográficos")
public class demograficosController {

    @Autowired
    private RespostaRequisicao service;

    @Autowired
    private demograficosService demograficosService;

    /**
     * Busca a população total residente no município informado.
     *
     * O método envia o nome do município para o serviço de demográficos e retorna
     * o valor em uma resposta padrão. Se não houver registro, retorna 204.
     *
     * @param municipio nome do município para busca textual
     * @return ResponseEntity com a população total ou 204 quando não houver resultado
     */
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

    /**
     * Busca a população total atualizada do estado.
     *
     * Retorna um mapa com os dados demográficos do estado no formato padrão.
     * Se o serviço não encontrar dados, devolve 204.
     *
     * @return ResponseEntity com a população total do estado ou 204 quando não houver dados
     */
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

    /**
     * Busca a quantidade total de homens no município informado.
     *
     * O serviço retorna o valor demográfico do município e o controller monta a
     * resposta padrão. Quando não há dados, responde 204.
     *
     * @param municipio nome do município para busca textual
     * @return ResponseEntity com a quantidade de homens ou 204 quando não houver dados
     */
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

    /**
     * Busca a quantidade total de mulheres no município informado.
     *
     * O valor retornado pelo serviço é envelopado na resposta padrão do projeto.
     * Se não existe dado para o município, a resposta é 204.
     *
     * @param municipio nome do município para busca textual
     * @return ResponseEntity com a quantidade de mulheres ou 204 quando não houver dados
     */
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

    /**
     * Busca a quantidade total de residentes rurais no município informado.
     *
     * O serviço de demográficos retorna o número de residentes rurais e o controller
     * monta a resposta padrão. Se não houver dado, retorna 204.
     *
     * @param municipio nome do município para busca textual
     * @return ResponseEntity com a quantidade de residentes rurais ou 204 quando não houver dados
     */
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

    /**
     * Busca o Índice de Desenvolvimento Humano (IDH) para o município informado.
     *
     * O valor retornado pelo serviço é encapsulado em um mapa e envelopado na
     * resposta padrão do projeto. Se não houver registro para o município,
     * retorna 204.
     *
     * @param municipio nome do município para busca textual
     * @return ResponseEntity com o IDH do município ou 204 quando não houver dados
     */
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

    /**
     * Busca a evolução do Índice de Desenvolvimento Humano (IDH) para o município informado.
     *
     * A consulta retorna um histórico de evolução do IDH no formato de mapa e o valor
     * é entregue na resposta padrão do projeto. Se não houver dados, responde 204.
     *
     * @param municipio nome do município para busca textual
     * @return ResponseEntity com a evolução do IDH ou 204 quando não houver dados
     */
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

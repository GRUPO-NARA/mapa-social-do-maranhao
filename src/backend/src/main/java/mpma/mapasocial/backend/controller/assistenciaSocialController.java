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
import org.springframework.web.bind.annotation.CrossOrigin;
import mpma.mapasocial.backend.service.RespostaRequisicao;
import mpma.mapasocial.backend.service.assistenciasocial.assistenciasocialService;

/**
 * Controller de Assistência Social responsável por expor endpoints REST de indicadores
 * sociais do Cadastro Único para municípios do Maranhão.
 *
 * As requisições são encaminhadas ao serviço de Assistência Social e o resultado é
 * empacotado no formato padrão de resposta do projeto.
 */
@RestController
@RequestMapping("/assistencia_social")
@CrossOrigin("http://localhost:3000")
@Tag(name = "Assistência Social", description = "Endpoints para dados de assistência social")
public class assistenciaSocialController {

    @Autowired
    private RespostaRequisicao service;

    @Autowired
    private assistenciasocialService assistenciaSocialService;

    /**
     * Busca a quantidade de famílias em situação de rua inscritas no Cadastro Único
     * para o município informado.
     *
     * O método delega a consulta ao serviço de Assistência Social e, em caso de sucesso,
     * retorna o dado envelopado em `RespostaRequisicao.ObjetoDeResposta`.
     * Se o resultado for `null`, retorna 204. Se ocorrer uma exceção no `try-catch`,
     * a mensagem de erro é encapsulada e devolvida como 500.
     *
     * @param municipio nome do município para busca textual
     * @return ResponseEntity com o resultado da consulta ou o erro tratado
     */
    @Operation(
            summary = "Busca famílias em situação de rua inscritas no Cadastro Único",
            description = "Retorna a quantidade de famílias em situação de rua inscritas no Cadastro Único com base no município informado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados encontrados com sucesso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"quantidade\": 150}"))),
            @ApiResponse(responseCode = "204", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "400", description = "Parâmetro município não informado ou inválido"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/buscarFamiliasEmSituacaoDeRua")
    public ResponseEntity<?> buscarFamiliasEmSituacaoDeRua(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @RequestParam("municipio") String municipio
    ) {
        try {
            Long quantidade = assistenciaSocialService.FamiliasEmSituacaoDeRua(municipio);
            if (quantidade == null) {
                return ResponseEntity.noContent().build();
            }
            var resposta = service.criarCorpo(
                    "200",
                    "famílias em situação de rua do município de " + municipio,
                    new RespostaRequisicao.ObjetoDeResposta(quantidade)
            );
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            var respostaErro = service.criarCorpo(
                    "500",
                    "Ocorreu um erro ao processar a requisição",
                    new RespostaRequisicao.ObjetoDeResposta(e.getMessage())
            );
            return ResponseEntity.internalServerError().body(respostaErro);
        }
    }

    /**
     * Busca a quantidade de domicílios com energia elétrica para o município informado.
     *
     * A chamada passa o parâmetro para o serviço de Assistência Social e utiliza a
     * resposta padrão do projeto. O `try-catch` captura falhas operacionais e
     * retorna uma mensagem de erro encapsulada em 500.
     *
     * @param municipio nome do município para busca textual
     * @return ResponseEntity com o resultado da consulta ou o erro tratado
     */
    @Operation(
            summary = "Busca domicílios com energia elétrica",
            description = "Retorna a quantidade de domicílios com energia elétrica com base no município informado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados encontrados com sucesso"),
            @ApiResponse(responseCode = "204", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/buscarDomiciliosComEnergiaEletrica")
    public ResponseEntity<?> buscarDomiciliosComEnergiaEletrica(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @RequestParam("municipio") String municipio
    ) {
        try {
            Long quantidade = assistenciaSocialService.DomiciliosComEnergiaEletrica(municipio);
            if (quantidade == null) {
                return ResponseEntity.noContent().build();
            }
            var resposta = service.criarCorpo(
                    "200",
                    "domicílios com energia elétrica do município de " + municipio,
                    new RespostaRequisicao.ObjetoDeResposta(quantidade)
            );
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            var respostaErro = service.criarCorpo(
                    "500",
                    "Ocorreu um erro ao processar a requisição",
                    new RespostaRequisicao.ObjetoDeResposta(e.getMessage())
            );
            return ResponseEntity.internalServerError().body(respostaErro);
        }
    }

    /**
     * Busca a quantidade de famílias em situação de trabalho infantil inscritas no Cadastro Único
     * para o município informado.
     *
     * Em caso de sucesso, o valor é encapsulado em `RespostaRequisicao.ObjetoDeResposta`.
     * Se o serviço retornar `null`, a resposta é 204. O bloco `try-catch` garante que
     * exceções geradas durante a execução sejam devolvidas como erro interno.
     *
     * @param municipio nome do município para busca textual
     * @return ResponseEntity com o resultado da consulta ou o erro tratado
     */
    @Operation(
            summary = "Busca famílias em situação de trabalho infantil",
            description = "Retorna a quantidade de famílias em situação de trabalho infantil inscritas no Cadastro Único com base no município informado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados encontrados com sucesso"),
            @ApiResponse(responseCode = "204", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/buscarFamiliasEmSituacaoDeTrabalhoInfantil")
    public ResponseEntity<?> buscarFamiliasEmSituacaoDeTrabalhoInfantil(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @RequestParam("municipio") String municipio
    ) {
        try {
            Long quantidade = assistenciaSocialService.FamiliasEmSituacaoDeTrabalhoInfantil(municipio);
            if (quantidade == null) {
                return ResponseEntity.noContent().build();
            }
            var resposta = service.criarCorpo(
                    "200",
                    "famílias em situação de trabalho infantil do município de " + municipio,
                    new RespostaRequisicao.ObjetoDeResposta(quantidade)
            );
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            var respostaErro = service.criarCorpo(
                    "500",
                    "Ocorreu um erro ao processar a requisição",
                    new RespostaRequisicao.ObjetoDeResposta(e.getMessage())
            );
            return ResponseEntity.internalServerError().body(respostaErro);
        }
    }

    /**
     * Busca a quantidade de beneficiários do programa auxiliar gás para brasileiros
     * no município informado.
     *
     * A requisição é repassada ao serviço e o resultado é retornado no formato
     * padrão de resposta. Se ocorrer uma exceção, o erro é capturado e a mensagem
     * é enviada no corpo do ResponseEntity com status 500.
     *
     * @param municipio nome do município para busca textual
     * @return ResponseEntity com o resultado da consulta ou o erro tratado
     */
    @Operation(
            summary = "Busca programa auxiliar gás para brasileiros",
            description = "Retorna a quantidade de beneficiários do programa auxiliar gás para brasileiros com base no município informado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados encontrados com sucesso"),
            @ApiResponse(responseCode = "204", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/buscarAuxilioGasParaBrasileiro")
    public ResponseEntity<?> buscarAuxilioGasParaBrasileiro(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @RequestParam("municipio") String municipio
    ) {
        try {
            Long quantidade = assistenciaSocialService.AuxilioGasParaBrasileiroPorMunicipio(municipio);
            if (quantidade == null) {
                return ResponseEntity.noContent().build();
            }
            var resposta = service.criarCorpo(
                    "200",
                    "beneficiários do programa auxiliar gás do município de " + municipio,
                    new RespostaRequisicao.ObjetoDeResposta(quantidade)
            );
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            var respostaErro = service.criarCorpo(
                    "500",
                    "Ocorreu um erro ao processar a requisição",
                    new RespostaRequisicao.ObjetoDeResposta(e.getMessage())
            );
            return ResponseEntity.internalServerError().body(respostaErro);
        }
    }

    /**
     * Busca a quantidade de pessoas com deficiência cadastradas no Cadastro Único
     * para o município informado.
     *
     * A consulta é repassada ao serviço de Assistência Social e, em caso de sucesso,
     * fornece o valor envelopado em `RespostaRequisicao.ObjetoDeResposta`.
     * Se o retorno for `null`, responde 204. Qualquer exceção capturada no `catch`
     * é devolvida como erro interno padrão.
     *
     * @param municipio nome do município para busca textual
     * @return ResponseEntity com o resultado da consulta ou o erro tratado
     */
    @Operation(
            summary = "Busca pessoas com deficiência cadastradas no Cadastro Único",
            description = "Retorna a quantidade de pessoas com deficiência cadastradas no Cadastro Único com base no município informado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados encontrados com sucesso"),
            @ApiResponse(responseCode = "204", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/buscarPessoasComDeficiencia")
    public ResponseEntity<?> buscarPessoasComDeficiencia(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @RequestParam("municipio") String municipio
    ) {
        try {
            Long quantidade = assistenciaSocialService.PessoasComDeficienciaCadastradasCadastroUnico(municipio);
            if (quantidade == null) {
                return ResponseEntity.noContent().build();
            }
            var resposta = service.criarCorpo(
                    "200",
                    "pessoas com deficiência do município de " + municipio,
                    new RespostaRequisicao.ObjetoDeResposta(quantidade)
            );
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            var respostaErro = service.criarCorpo(
                    "500",
                    "Ocorreu um erro ao processar a requisição",
                    new RespostaRequisicao.ObjetoDeResposta(e.getMessage())
            );
            return ResponseEntity.internalServerError().body(respostaErro);
        }
    }

    /**
     * Busca a quantidade de pessoas inscritas no Cadastro Único por raça e cor
     * para o município informado.
     *
     * O método encapsula o resultado no padrão de resposta do projeto e retorna
     * 204 quando não houver dados. Se ocorrer exceção, o `catch` faz o manejo do erro.
     *
     * @param municipio nome do município para busca textual
     * @return ResponseEntity com o resultado da consulta ou o erro tratado
     */
    @Operation(
            summary = "Busca pessoas inscritas no Cadastro Único por raça e cor",
            description = "Retorna a quantidade de pessoas inscritas no Cadastro Único por raça e cor com base no município informado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados encontrados com sucesso"),
            @ApiResponse(responseCode = "204", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/buscarPessoasPorRacaECor")
    public ResponseEntity<?> buscarPessoasPorRacaECor(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @RequestParam("municipio") String municipio
    ) {
        try {
            Long quantidade = assistenciaSocialService.PessoasInscritasNoCadastroUnicoPorRacaECor(municipio);
            if (quantidade == null) {
                return ResponseEntity.noContent().build();
            }
            var resposta = service.criarCorpo(
                    "200",
                    "pessoas inscritas no Cadastro Único por raça e cor do município de " + municipio,
                    new RespostaRequisicao.ObjetoDeResposta(quantidade)
            );
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            var respostaErro = service.criarCorpo(
                    "500",
                    "Ocorreu um erro ao processar a requisição",
                    new RespostaRequisicao.ObjetoDeResposta(e.getMessage())
            );
            return ResponseEntity.internalServerError().body(respostaErro);
        }
    }

    /**
     * Busca a quantidade de pessoas inscritas no Cadastro Único por sexo
     * para o município informado.
     *
     * O fluxo captura exceções no bloco `try-catch` e mantém a resposta no padrão
     * do projeto, retornando 500 com a mensagem de exceção encapsulada quando necessário.
     *
     * @param municipio nome do município para busca textual
     * @return ResponseEntity com o resultado da consulta ou o erro tratado
     */
    @Operation(
            summary = "Busca pessoas inscritas no Cadastro Único por sexo",
            description = "Retorna a quantidade de pessoas inscritas no Cadastro Único por sexo com base no município informado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados encontrados com sucesso"),
            @ApiResponse(responseCode = "204", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/buscarPessoasPorSexo")
    public ResponseEntity<?> buscarPessoasPorSexo(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @RequestParam("municipio") String municipio
    ) {
        try {
            Long quantidade = assistenciaSocialService.PessoasInscritasNoCadastroUnicoPorSexo(municipio);
            if (quantidade == null) {
                return ResponseEntity.noContent().build();
            }
            var resposta = service.criarCorpo(
                    "200",
                    "pessoas inscritas no Cadastro Único por sexo do município de " + municipio,
                    new RespostaRequisicao.ObjetoDeResposta(quantidade)
            );
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            var respostaErro = service.criarCorpo(
                    "500",
                    "Ocorreu um erro ao processar a requisição",
                    new RespostaRequisicao.ObjetoDeResposta(e.getMessage())
            );
            return ResponseEntity.internalServerError().body(respostaErro);
        }
    }

    /**
     * Busca o total de famílias inscritas no Cadastro Único para o município informado.
     *
     * O método delega a consulta ao serviço e monta a resposta padrão. Quando o serviço
     * retorna `null`, a resposta é 204. Exceções são capturadas e retornadas como 500.
     *
     * @param municipio nome do município para busca textual
     * @return ResponseEntity com o resultado da consulta ou o erro tratado
     */
    @Operation(
            summary = "Busca total de famílias inscritas no Cadastro Único",
            description = "Retorna a quantidade total de famílias inscritas no Cadastro Único com base no município informado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados encontrados com sucesso"),
            @ApiResponse(responseCode = "204", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/buscarTotalDeFamilias")
    public ResponseEntity<?> buscarTotalDeFamilias(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @RequestParam("municipio") String municipio
    ) {
        try {
            Long quantidade = assistenciaSocialService.TotalDeFamiliasInscritasNoCadastroUnico(municipio);
            if (quantidade == null) {
                return ResponseEntity.noContent().build();
            }
            var resposta = service.criarCorpo(
                    "200",
                    "total de famílias inscritas no Cadastro Único do município de " + municipio,
                    new RespostaRequisicao.ObjetoDeResposta(quantidade)
            );
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            var respostaErro = service.criarCorpo(
                    "500",
                    "Ocorreu um erro ao processar a requisição",
                    new RespostaRequisicao.ObjetoDeResposta(e.getMessage())
            );
            return ResponseEntity.internalServerError().body(respostaErro);
        }
    }

    /**
     * Busca a quantidade de domicílios com água encanada para o município informado.
     *
     * O resultado é enviado na estrutura padrão do projeto. Se não existir dado,
     * responde 204. Caso ocorra exceção, responde 500 com a mensagem encapsulada.
     *
     * @param municipio nome do município para busca textual
     * @return ResponseEntity com o resultado da consulta ou o erro tratado
     */
    @Operation(
            summary = "Busca domicílios com água encanada",
            description = "Retorna a quantidade de domicílios com água encanada com base no município informado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados encontrados com sucesso"),
            @ApiResponse(responseCode = "204", description = "Dados não encontrados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/buscarDomiciliosComAguaEncanada")
    public ResponseEntity<?> buscarDomiciliosComAguaEncanada(
            @Parameter(
                    name = "municipio",
                    description = "nome do município (ex: São Luís)",
                    example = "São Luís",
                    required = true
            )
            @RequestParam("municipio") String municipio
    ) {
        try {
            Long quantidade = assistenciaSocialService.DomiciliosComAguaEncanada(municipio);
            if (quantidade == null) {
                return ResponseEntity.noContent().build();
            }
            var resposta = service.criarCorpo(
                    "200",
                    "domicílios com água encanada do município de " + municipio,
                    new RespostaRequisicao.ObjetoDeResposta(quantidade)
            );
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            var respostaErro = service.criarCorpo(
                    "500",
                    "Ocorreu um erro ao processar a requisição",
                    new RespostaRequisicao.ObjetoDeResposta(e.getMessage())
            );
            return ResponseEntity.internalServerError().body(respostaErro);
        }
    }
}

package mpma.mapasocial.backend.controller;

import mpma.mapasocial.backend.dto.DadosPrincipaisSidraDTO;
import mpma.mapasocial.backend.service.Informacoes;
import mpma.mapasocial.backend.service.RespostaRequisicao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável por expor os endpoints de informações relacionadas ao SIDRA.
 *
 * Este controller delega a obtenção dos dados ao serviço {@link Informacoes}
 * e empacota a resposta usando {@link RespostaRequisicao} para manter o contrato
 * de API consistente com outros endpoints do backend.
 */
@RestController
@RequestMapping("/informacoes")
@CrossOrigin(origins = "http://localhost:3000")
public class informacoesController {

    @Autowired
    private Informacoes informacoes;

    @Autowired
    private RespostaRequisicao respostaRequisicao;

    /**
     * Endpoint REST que retorna os dados principais do SIDRA para o município informado.
     *
     * @param municipio Nome do município alvo da consulta.
     * @return ResponseEntity com corpo de resposta padronizado ou sem conteúdo quando não há dados.
     */
    @GetMapping("/dadosPrincipaisSIDRA")
    public ResponseEntity<?> dadosPrincipaisSIDRA(@RequestParam("municipio") String municipio){
        List<DadosPrincipaisSidraDTO> dados = informacoes.dadosPrincipaisSIDRA(municipio);

        if (dados == null || dados.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        var resposta = respostaRequisicao.criarCorpo(
                "200",
                "Dados Principais do SIDRA para o município de " + municipio,
                new RespostaRequisicao.ObjetoDeResposta(dados)
        );

        return ResponseEntity.ok().body(resposta);
    }
}

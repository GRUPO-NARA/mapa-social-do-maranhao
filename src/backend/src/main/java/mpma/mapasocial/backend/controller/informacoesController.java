package mpma.mapasocial.backend.controller;

import mpma.mapasocial.backend.dto.DadosPrincipaisSidraDTO;
import mpma.mapasocial.backend.service.Informacoes;
import mpma.mapasocial.backend.service.RespostaRequisicao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/informacoes")
@CrossOrigin(origins = "http://localhost:3000")
public class informacoesController {

    @Autowired
    private Informacoes informacoes;

    @Autowired
    private RespostaRequisicao respostaRequisicao;

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

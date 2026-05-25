package mpma.mapasocial.backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import mpma.mapasocial.backend.service.saude.saudeService;
import mpma.mapasocial.backend.service.RespostaRequisicao;

@RestController
@RequestMapping("/saude")
@Tag(name = "Saúde", description = "Endpoints para dados de saúde")
public class saudeController {

    @Autowired
    private RespostaRequisicao service;

    @Autowired
    private saudeService saudeService;

    /*
    @GetMapping("/buscarIdadeMediana")
    public ResponseEntity <?> buscarIdadeMediana(String ano, String municipio) {
        try {
            Double idadeMediana = saudeService.buscarIdadeMedianaPorMunicipio(ano, municipio);
            return service.gerarRespostaSucesso(idadeMediana);
        } catch (Exception e) {
            return service.gerarRespostaErro(e.getMessage());
        }
    }
    */




}

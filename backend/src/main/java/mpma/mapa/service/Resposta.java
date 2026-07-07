package mpma.mapa.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class Resposta {

    public HashMap<String, Object> CorpoDaResposta(String indicador, Object mensagem, String statusDaRequisicao) {

        HashMap<String, Object> corpoDaResposta = new HashMap<>();
        corpoDaResposta.put("Indicador da Requisição", indicador);
        corpoDaResposta.put("Resposta da Requisição", mensagem);
        corpoDaResposta.put("Status da Requisição", statusDaRequisicao);
        return corpoDaResposta;
    }

    public HashMap<String, Object> CorpoDaRespostaError(String indicador, String erro, String statusDaRequisicao) {
        String erroSeguro = (erro != null) ? erro : "Erro interno não especificado (Null)";
        return new HashMap<>(Map.of(
           "Indicador da Requisição", indicador,
           "Mensagem", "Não foi possível obter os dados solicitados",
           "Erro", erroSeguro,
           "Status da Requisição", statusDaRequisicao
        ));
    }

}

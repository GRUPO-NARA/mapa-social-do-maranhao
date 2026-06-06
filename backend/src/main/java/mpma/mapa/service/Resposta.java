package mpma.mapa.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class Resposta {

    public HashMap<String, Object> CorpoDaResposta(String indicador, Object mensagem, String statusDaRequisicao) {

        return new HashMap<>(Map.of(
                "Indicador da Requisição", indicador,
                "Resposta da Requisição", mensagem,
                "Status da Requisição", statusDaRequisicao
        ));
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

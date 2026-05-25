package mpma.mapasocial.backend.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RespostaRequisicao {


    public sealed interface dadosRequisitados permits ObjetoDeResposta{}

    public record ObjetoDeResposta(Object dados) implements dadosRequisitados{}

    public Map<String, Object> criarCorpo(String status, String indicadorNome, dadosRequisitados dados) {
        Map<String, Object> corpo = new HashMap<>();

        corpo.put("status", status);
        corpo.put("indicador", indicadorNome);
        corpo.put("resposta", dados);

        return corpo;
    }
}

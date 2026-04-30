package mpma.mapasocial.backend.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class gerarRespostaRequisicaoService {


    public sealed interface RespostaRequisicao permits RespostaTexto, RespostaLong, RespostaDouble{}

    public record RespostaTexto(String valor) implements RespostaRequisicao {}
    public record RespostaLong(Long valor) implements RespostaRequisicao {}
    public record RespostaDouble(Double valor) implements RespostaRequisicao{}

    public Map<String, Object> criarCorpo(String status, String indicadorNome, RespostaRequisicao valor) {
        Map<String, Object> corpo = new HashMap<>();

        corpo.put("status", status);
        corpo.put("indicador", indicadorNome);
        corpo.put("resposta", valor);

        return corpo;
    }
}

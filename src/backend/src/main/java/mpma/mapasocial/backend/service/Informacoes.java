package mpma.mapasocial.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mpma.mapasocial.backend.dto.DadosPrincipaisSidraDTO;
import mpma.mapasocial.backend.repository.InformacoesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Informacoes {

    @Autowired
    private InformacoesRepository informacoesRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public List<DadosPrincipaisSidraDTO> dadosPrincipaisSIDRA(String municipio) {
        String jsonResult = informacoesRepository.buscarDadosPrincipaisSIDRA(municipio);

        if (jsonResult == null || jsonResult.isEmpty()) {
            return new ArrayList<>();
        }

        List<DadosPrincipaisSidraDTO> resultado = new ArrayList<>();

        try {
            JsonNode arrayNode = objectMapper.readTree(jsonResult);
            // Mudar o formato dos dados do valor para Object
            if (arrayNode.isArray()) {
                for (JsonNode node : arrayNode) {
                    String indicador = node.get("indicador").asText();
                    String valor = node.get("valor").asText();
                    String dataColeta = node.get("data_coleta").asText();

                    resultado.add(new DadosPrincipaisSidraDTO(indicador, valor, dataColeta));
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao processar dados SIDRA: " + e.getMessage());
            e.printStackTrace();
        }

        return resultado;
    }
}

package mpma.mapasocial.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mpma.mapasocial.backend.dto.DadosPrincipaisSidraDTO;
import mpma.mapasocial.backend.repository.InformacoesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Serviço responsável por orquestrar a consulta de informações SIDRA para um município.
 *
 * A classe delega a obtenção dos dados ao repositório, faz parse do JSON retornado
 * e converte o resultado em uma lista de DTOs usados pela camada de apresentação.
 */
@Service
public class Informacoes {

    @Autowired
    private InformacoesRepository informacoesRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Busca os principais dados SIDRA de um município e formata o JSON em DTOs.
     *
     * O método chama o repositório para obter uma string JSON. Se não houver conteúdo,
     * retorna lista vazia. Caso contrário, faz o parse do JSON em um array e itera cada
     * elemento, extraindo os campos esperados para montar os objetos de saída.
     *
     * @param municipio nome do município usado na consulta SIDRA
     * @return lista de DadosPrincipaisSidraDTO contendo indicador, valor e data de coleta
     */
    public List<DadosPrincipaisSidraDTO> dadosPrincipaisSIDRA(String municipio) {
        String jsonResult = informacoesRepository.buscarDadosPrincipaisSIDRA(municipio);

        if (jsonResult == null || jsonResult.isEmpty()) {
            return new ArrayList<>();
        }

        List<DadosPrincipaisSidraDTO> resultado = new ArrayList<>();

        try {
            JsonNode arrayNode = objectMapper.readTree(jsonResult);
            // Parse do JSON retornado pelo repositório e conversão em lista de DTOs
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

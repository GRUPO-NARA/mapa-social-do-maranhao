package mpma.mapasocial.backend.service.demograficos;

import com.fasterxml.jackson.core.JsonProcessingException;
import mpma.mapasocial.backend.dto.IDHMunicipalDTO;
import mpma.mapasocial.backend.dto.PopulacaoEstadualDTO;
import mpma.mapasocial.backend.repository.demograficos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;

@Service
public class demograficosService {

    @Autowired
    private PopulacaoResidenteRepository populacaoResidenteRepository;

    @Autowired
    private QuantidadeDeHomensRepository quantidadeDeHomensRepository;

    @Autowired
    private QuantidadeDeMulheresRepository quantidadeDeMulheresRepository;

    @Autowired
    private QuantidadeDeResidentesRuraisRepository quantidadeDeResidentesRuraisRepository;

    @Autowired
    private IndiceDeDesenvolvimentoHumanoRepository indiceDeDesenvolvimentoHumanoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public Long populacaoTotalDoMunicipio(@Param("municipio") String municipio){
        return populacaoResidenteRepository.buscarPopulacaoResidente(municipio);
    }

    public HashMap<String, Object> populacaoTotalAtualizadaDoEstado() {

        String jsonResult = populacaoResidenteRepository.buscaPopulacaoResidenteEstadualDosDadosMaisRecentes();

        if (jsonResult == null) {
            return null;
        }

        PopulacaoEstadualDTO dto = objectMapper.readValue(jsonResult, PopulacaoEstadualDTO.class);

        return objectMapper.convertValue(dto, new TypeReference<HashMap<String, Object>>() {});

    }
    
    public Long quantidadeDeHomensDoMunicipio(@Param("municipio") String municipio){
        return quantidadeDeHomensRepository.buscarQuantidadeDeHomensPorMunicipio(municipio);
    }

    public Long quantidadeDeMulheresDoMunicipio(@Param("municipio") String municipio){
        return quantidadeDeMulheresRepository.buscarQuantidadeDeMulheresPorMunicipio(municipio);
    }

    public Long quantidadeDeResidentesRuraisDoMunicipio(@Param("municipio") String municipio){
        return quantidadeDeResidentesRuraisRepository.buscarQuantidadeDeResidentesRuraisPorMunicipio(municipio);
    }

    public HashMap<String, Object> indiceDeDesenvolvimentoHumanoDoMunicipio(@Param("municipio") String municipio){
        String idhMunicipio = indiceDeDesenvolvimentoHumanoRepository.buscarIndiceDeDesenvolvimentoHumanoPorMunicipio(municipio);

        if (idhMunicipio == null){
            return null;
        }

        IDHMunicipalDTO dto = objectMapper.readValue(idhMunicipio, IDHMunicipalDTO.class);

        return objectMapper.convertValue(dto, new TypeReference<HashMap<String, Object>>() {});
    }

    public HashMap<String, Object> evolucaoIndiceDeDesenvolvimentoHumanoDoMunicipio(@Param("municipio") String municipio){
        List<String> jsonLinhas = indiceDeDesenvolvimentoHumanoRepository.buscarEvolucaoIndiceDeDesenvolvimentoHumanoPorMunicipio(municipio);

        if (jsonLinhas == null || jsonLinhas.isEmpty()) {
            return null;
        }

        HashMap<String, Object> resultado = new HashMap<>();

        try {
            for (String json : jsonLinhas) {

                JsonNode node = objectMapper.readTree(json);

                String referencia = node.get("referencia").asText();
                double idh = node.get("IDH").asDouble();

                resultado.put(referencia, idh);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar dados de evolução do IDH", e);
        }

        return resultado;
    }
}

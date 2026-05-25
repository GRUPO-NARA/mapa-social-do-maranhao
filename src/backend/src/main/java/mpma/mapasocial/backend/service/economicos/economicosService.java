package mpma.mapasocial.backend.service.economicos;

import mpma.mapasocial.backend.dto.PibAgregadoDTO;
import mpma.mapasocial.backend.dto.PibDTO;
import mpma.mapasocial.backend.repository.economicos.ProdutoInternoBrutoMunicipalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;

@Service
public class economicosService {
    @Autowired
    private ProdutoInternoBrutoMunicipalRepository produtoInternoBrutoMunicipalRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public HashMap<String, Object> produtoInternoBruto(@Param("municipio") String municipio){
        String pibMunicipio = produtoInternoBrutoMunicipalRepository.buscarPIB(municipio);
        if (pibMunicipio == null) {
            return null;
        }
        PibDTO dto = objectMapper.readValue(pibMunicipio, PibDTO.class);
        return objectMapper.convertValue(dto, new TypeReference<HashMap<String, Object>>() {});
    }

    public HashMap<String, Object> produtoInternoBrutoAgregadoRecente() {
        String resultado_json = produtoInternoBrutoMunicipalRepository.buscarPIBAgregadoRecente();

        if (resultado_json == null) {
            return null;
        }

        PibAgregadoDTO dto = objectMapper.readValue(resultado_json, PibAgregadoDTO.class);

        return objectMapper.convertValue(dto, new TypeReference<HashMap<String, Object>>() {});
    }
}

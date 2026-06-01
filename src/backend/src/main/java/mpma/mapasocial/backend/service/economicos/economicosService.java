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

/**
 * Serviço da camada Econômicos responsável por orquestrar consultas de dados de PIB.
 *
 * Esta classe atua como ponte entre o Controller e o Repository para os indicadores
 * de Produto Interno Bruto, isolando a chamada à query nativa e o processamento de retorno.
 */
@Service
public class economicosService {
    @Autowired
    private ProdutoInternoBrutoMunicipalRepository produtoInternoBrutoMunicipalRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Ponte de negócio entre Controller e Repository para o indicador de PIB municipal.
     *
     * O método recebe o nome do município, repassa para a query nativa do repositório de PIB
     * e transforma o JSON retornado em um mapa para a camada de API.
     * Ele foi projetado para entregar o panorama mais recente disponível no banco,
     * sem exigir filtro explícito de ano na chamada.
     *
     * @param municipio nome do município com grafia correta para busca com ILIKE (ex: 'São Luís')
     * @return HashMap<String, Object> com os dados do PIB municipal mais recente ou null caso nenhum registro seja encontrado
     */
    public HashMap<String, Object> produtoInternoBruto(@Param("municipio") String municipio){
        String pibMunicipio = produtoInternoBrutoMunicipalRepository.buscarPIB(municipio);
        if (pibMunicipio == null) {
            return null;
        }
        PibDTO dto = objectMapper.readValue(pibMunicipio, PibDTO.class);
        return objectMapper.convertValue(dto, new TypeReference<HashMap<String, Object>>() {});
    }

    /**
     * Ponte de negócio entre Controller e Repository para o indicador de PIB agregado estadual.
     *
     * O método consulta o repositório de PIB agregado mais recente, converte o JSON retornado
     * em DTO e torna o resultado disponível para a camada de API.
     * Ele isola a complexidade de desserialização e garante a devolução do dado mais recente disponível.
     *
     * @return HashMap<String, Object> com o PIB agregado mais recente ou null caso nenhum registro seja encontrado
     */
    public HashMap<String, Object> produtoInternoBrutoAgregadoRecente() {
        String resultado_json = produtoInternoBrutoMunicipalRepository.buscarPIBAgregadoRecente();

        if (resultado_json == null) {
            return null;
        }

        PibAgregadoDTO dto = objectMapper.readValue(resultado_json, PibAgregadoDTO.class);

        return objectMapper.convertValue(dto, new TypeReference<HashMap<String, Object>>() {});
    }
}

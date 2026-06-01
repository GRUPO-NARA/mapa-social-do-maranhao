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

/**
 * Serviço da camada Demográficos responsável por orquestrar consultas de dados demográficos.
 *
 * Cada método atua como ponte entre o Controller e o Repository, recebendo o município e
 * repassando a chamada para a query nativa apropriada, isolando a complexidade de busca.
 */
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

    /**
     * Ponte de negócio entre Controller e Repository para o indicador de população total do município.
     *
     * Recebe o nome do município, repassa para a query do repositório correspondente e isola a complexidade de busca.
     * O método retorna o panorama estatístico mais recente disponível no banco de dados para esse indicador.
     *
     * @param municipio nome do município com grafia correta para busca com ILIKE (ex: 'São Luís')
     * @return Long valor numérico absoluto da população residente consolidada ou null caso nenhum registro seja encontrado
     */
    public Long populacaoTotalDoMunicipio(@Param("municipio") String municipio){
        return populacaoResidenteRepository.buscarPopulacaoResidente(municipio);
    }

    /**
     * Ponte de negócio entre Controller e Repository para o indicador de população estadual atualizada.
     *
     * Busca o JSON mais recente de população estadual no repositório, converte para DTO e retorna como mapa.
     * O método isola a complexidade de desserialização e entrega diretamente a estrutura pronta para a camada de API.
     *
     * @return HashMap<String, Object> com os dados de população estadual mais recentes ou null se não houver registro
     */
    public HashMap<String, Object> populacaoTotalAtualizadaDoEstado() {

        String jsonResult = populacaoResidenteRepository.buscaPopulacaoResidenteEstadualDosDadosMaisRecentes();

        if (jsonResult == null) {
            return null;
        }

        PopulacaoEstadualDTO dto = objectMapper.readValue(jsonResult, PopulacaoEstadualDTO.class);

        return objectMapper.convertValue(dto, new TypeReference<HashMap<String, Object>>() {});

    }
    
    /**
     * Ponte de negócio entre Controller e Repository para o indicador de quantidade de homens do município.
     *
     * Recebe o nome do município, repassa para a query do repositório correspondente e isola a complexidade de busca.
     * O método retorna o panorama estatístico mais recente disponível no banco de dados para esse indicador.
     *
     * @param municipio nome do município com grafia correta para busca com ILIKE (ex: 'São Luís')
     * @return Long valor numérico absoluto da quantidade de homens consolidada ou null caso nenhum registro seja encontrado
     */
    public Long quantidadeDeHomensDoMunicipio(@Param("municipio") String municipio){
        return quantidadeDeHomensRepository.buscarQuantidadeDeHomensPorMunicipio(municipio);
    }

    /**
     * Ponte de negócio entre Controller e Repository para o indicador de quantidade de mulheres do município.
     *
     * Recebe o nome do município, repassa para a query do repositório correspondente e isola a complexidade de busca.
     * O método retorna o panorama estatístico mais recente disponível no banco de dados para esse indicador.
     *
     * @param municipio nome do município com grafia correta para busca com ILIKE (ex: 'São Luís')
     * @return Long valor numérico absoluto da quantidade de mulheres consolidada ou null caso nenhum registro seja encontrado
     */
    public Long quantidadeDeMulheresDoMunicipio(@Param("municipio") String municipio){
        return quantidadeDeMulheresRepository.buscarQuantidadeDeMulheresPorMunicipio(municipio);
    }

    /**
     * Ponte de negócio entre Controller e Repository para o indicador de quantidade de residentes rurais.
     *
     * Recebe o nome do município, repassa para a query do repositório correspondente e isola a complexidade de busca.
     * O método retorna o panorama estatístico mais recente disponível no banco de dados para esse indicador.
     *
     * @param municipio nome do município com grafia correta para busca com ILIKE (ex: 'São Luís')
     * @return Long valor numérico absoluto da quantidade de residentes rurais consolidada ou null caso nenhum registro seja encontrado
     */
    public Long quantidadeDeResidentesRuraisDoMunicipio(@Param("municipio") String municipio){
        return quantidadeDeResidentesRuraisRepository.buscarQuantidadeDeResidentesRuraisPorMunicipio(municipio);
    }

    /**
     * Ponte de negócio entre Controller e Repository para o indicador de Índice de Desenvolvimento Humano (IDH) municipal.
     *
     * Recebe o nome do município, repassa para a query do repositório correspondente e isola a complexidade de busca e desserialização.
     * O método retorna o panorama estatístico mais recente disponível no banco de dados para esse indicador.
     *
     * @param municipio nome do município com grafia correta para busca com ILIKE (ex: 'São Luís')
     * @return HashMap<String, Object> representando o IDH municipal mais recente ou null caso nenhum registro seja encontrado
     */
    public HashMap<String, Object> indiceDeDesenvolvimentoHumanoDoMunicipio(@Param("municipio") String municipio){
        String idhMunicipio = indiceDeDesenvolvimentoHumanoRepository.buscarIndiceDeDesenvolvimentoHumanoPorMunicipio(municipio);

        if (idhMunicipio == null){
            return null;
        }

        IDHMunicipalDTO dto = objectMapper.readValue(idhMunicipio, IDHMunicipalDTO.class);

        return objectMapper.convertValue(dto, new TypeReference<HashMap<String, Object>>() {});
    }

    /**
     * Ponte de negócio entre Controller e Repository para a evolução histórica do Índice de Desenvolvimento Humano (IDH).
     *
     * Recebe o nome do município, repassa para a query do repositório correspondente e isola a complexidade de busca e processamento dos JSONs retornados.
     * O método retorna a série histórica de evolução do IDH ao longo de todos os anos disponíveis no banco.
     *
     * @param municipio nome do município com grafia correta para busca com ILIKE (ex: 'São Luís')
     * @return HashMap<String, Object> com referência ano/valor do IDH ou null se nenhum registro for encontrado
     */
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

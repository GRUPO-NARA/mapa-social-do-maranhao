package mpma.mapa.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mpma.mapa.dto.PredicaoDTO;
import mpma.mapa.exception.ServicoPredicaoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.Comparator;
import java.util.List;

@Service
public class PredicaoService {

    private static final int QUANTIDADE_MINIMA_PONTOS = 5;

    private final InformacoesService informacoesService;
    private final ObjectMapper objectMapper;
    private final RestClient apiPython;

    public PredicaoService(
            InformacoesService informacoesService,
            ObjectMapper objectMapper,
            RestClient.Builder restClientBuilder,
            @Value("${api.python.url}") String apiPythonUrl
    ) {
        this.informacoesService = informacoesService;
        this.objectMapper = objectMapper;

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5_000);
        requestFactory.setReadTimeout(30_000);

        this.apiPython = restClientBuilder
                .baseUrl(apiPythonUrl)
                .requestFactory(requestFactory)
                .build();
    }

    public PredicaoDTO.Resposta prever(
            String schema,
            String indicador,
            String municipio,
            Integer ano
    ) {
        List<PredicaoDTO.PontoHistorico> historico = informacoesService
                .buscarEvolucaoDoIndicador(schema, indicador, municipio)
                .stream()
                .map(this::converterPontoHistorico)
                .sorted(Comparator.comparing(PredicaoDTO.PontoHistorico::referencia))
                .toList();

        if (historico.size() < QUANTIDADE_MINIMA_PONTOS) {
            throw new IllegalArgumentException(
                    "São necessários pelo menos " + QUANTIDADE_MINIMA_PONTOS
                            + " registros históricos para gerar uma previsão"
            );
        }

        boolean indicadorPercentual = indicador.startsWith("taxa_");
        PredicaoDTO.Requisicao requisicao = new PredicaoDTO.Requisicao(
                ano,
                historico,
                indicadorPercentual ? 0.0 : null,
                indicadorPercentual ? 100.0 : null
        );

        try {
            PredicaoDTO.Resposta resposta = apiPython
                    .post()
                    .uri("/predicao")
                    .body(requisicao)
                    .retrieve()
                    .body(PredicaoDTO.Resposta.class);

            if (resposta == null) {
                throw new ServicoPredicaoException("A API Python retornou uma resposta vazia");
            }

            return resposta;
        } catch (RestClientResponseException exception) {
            throw new ServicoPredicaoException(
                    "A API Python recusou a previsão: " + exception.getResponseBodyAsString(),
                    exception
            );
        } catch (ResourceAccessException exception) {
            throw new ServicoPredicaoException(
                    "A API Python está indisponível no momento",
                    exception
            );
        }
    }

    private PredicaoDTO.PontoHistorico converterPontoHistorico(String jsonBruto) {
        try {
            JsonNode objeto = objectMapper.readTree(jsonBruto);
            JsonNode referencia = objeto.get("Referência");
            JsonNode valor = objeto.get("Valor");

            if (referencia == null || valor == null || !referencia.isNumber() || !valor.isNumber()) {
                throw new IllegalArgumentException("Registro histórico incompleto: " + jsonBruto);
            }

            return new PredicaoDTO.PontoHistorico(
                    referencia.asInt(),
                    valor.asDouble()
            );
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException(
                    "Não foi possível interpretar um registro da série histórica",
                    exception
            );
        }
    }
}

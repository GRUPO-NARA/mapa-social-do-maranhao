package mpma.mapa.service;

import mpma.mapa.dto.ClusterizacaoDTO;
import mpma.mapa.exception.ServicoClusterizacaoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Service
public class ClusterizacaoService {

    private static final int QUANTIDADE_MINIMA_MUNICIPIOS = 4;
    private static final int MINIMO_CLUSTERS = 2;
    private static final int MAXIMO_CLUSTERS = 5;

    private final InformacoesService informacoesService;
    private final RestClient apiPython;

    public ClusterizacaoService(
            InformacoesService informacoesService,
            RestClient.Builder restClientBuilder,
            @Value("${api.python.url}") String apiPythonUrl
    ) {
        this.informacoesService = informacoesService;

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5_000);
        requestFactory.setReadTimeout(30_000);

        this.apiPython = restClientBuilder
                .baseUrl(apiPythonUrl)
                .requestFactory(requestFactory)
                .build();
    }

    public ClusterizacaoDTO.Resposta clusterizar(String schema, String indicador) {
        List<ClusterizacaoDTO.PontoMunicipal> dados = informacoesService
                .buscarDadosParaClusterizacao(schema, indicador);
        Integer referencia = informacoesService.buscarReferenciaMaisRecente(schema, indicador);

        if (referencia == null || dados.size() < QUANTIDADE_MINIMA_MUNICIPIOS) {
            throw new IllegalArgumentException(
                    "São necessários dados de pelo menos " + QUANTIDADE_MINIMA_MUNICIPIOS
                            + " municípios na referência mais recente"
            );
        }

        ClusterizacaoDTO.Requisicao requisicao = new ClusterizacaoDTO.Requisicao(
                referencia,
                dados,
                MINIMO_CLUSTERS,
                MAXIMO_CLUSTERS
        );

        try {
            ClusterizacaoDTO.Resposta resposta = apiPython
                    .post()
                    .uri("/clusterizacao")
                    .body(requisicao)
                    .retrieve()
                    .body(ClusterizacaoDTO.Resposta.class);

            if (resposta == null) {
                throw new ServicoClusterizacaoException(
                        "A API Python retornou uma resposta vazia"
                );
            }

            return resposta;
        } catch (RestClientResponseException exception) {
            throw new ServicoClusterizacaoException(
                    "A API Python recusou a clusterização: "
                            + exception.getResponseBodyAsString(),
                    exception
            );
        } catch (ResourceAccessException exception) {
            throw new ServicoClusterizacaoException(
                    "A API Python está indisponível no momento",
                    exception
            );
        }
    }
}

package mpma.mapa.dto;

import java.util.List;

public final class ClusterizacaoDTO {

    private ClusterizacaoDTO() {
    }

    public record PontoMunicipal(
            String municipio,
            Double valor
    ) {
    }

    public record Requisicao(
            Integer referencia,
            List<PontoMunicipal> dados,
            Integer minimoClusters,
            Integer maximoClusters
    ) {
    }

    public record MetricaModelo(
            String algoritmo,
            Integer quantidadeClusters,
            Double silhouette
    ) {
    }

    public record ClusterMunicipal(
            String nome,
            Integer quantidadeMunicipios,
            String faixa,
            Double media,
            Double valorMinimo,
            Double valorMaximo,
            List<String> municipios
    ) {
    }

    public record Resposta(
            String algoritmoSelecionado,
            Integer quantidadeClusters,
            Double silhouette,
            Integer referencia,
            Integer totalMunicipios,
            List<MetricaModelo> modelosAvaliados,
            List<ClusterMunicipal> clusters
    ) {
    }
}

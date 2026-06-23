package mpma.mapa.dto;

import java.util.List;

public final class PredicaoDTO {

    private PredicaoDTO() {
    }

    public record PontoHistorico(
            Integer referencia,
            Double valor
    ) {
    }

    public record Requisicao(
            Integer anoPrevisao,
            List<PontoHistorico> dados,
            Double limiteInferior,
            Double limiteSuperior
    ) {
    }

    public record MetricasModelo(
            String modelo,
            Double mae,
            Double r2
    ) {
    }

    public record Resposta(
            String modelo,
            Integer ano,
            Double predicao,
            Double mae,
            Double r2,
            Integer pontosTreinamento,
            Integer referenciaInicial,
            Integer referenciaFinal,
            Boolean valorLimitado,
            List<MetricasModelo> modelosAvaliados
    ) {
    }
}

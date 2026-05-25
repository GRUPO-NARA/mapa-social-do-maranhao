package mpma.mapasocial.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DadosPrincipaisSidraDTO(
        @JsonProperty("indicador")
        String indicador,

        @JsonProperty("valor")
        String valor,

        @JsonProperty("data_coleta")
        String data_coleta
) {
}

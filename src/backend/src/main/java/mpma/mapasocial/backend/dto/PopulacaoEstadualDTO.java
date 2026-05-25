package mpma.mapasocial.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PopulacaoEstadualDTO(
        String referencia,

        @JsonProperty("populacao_estado")
        Long populacao_estado
) {
}

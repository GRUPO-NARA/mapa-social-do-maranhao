package mpma.mapasocial.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO que representa a população estadual para uma referência temporal.
 *
 * Inclui a referência e o valor total da população do estado.
 */
public record PopulacaoEstadualDTO(
        String referencia,

        @JsonProperty("populacao_estado")
        Long populacao_estado
) {
}

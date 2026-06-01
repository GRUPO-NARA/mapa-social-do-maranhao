package mpma.mapasocial.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO que representa o PIB de um município para uma referência temporal.
 *
 * O campo PIB é serializado como "PIB_municipio" no JSON de resposta.
 */
public record PibDTO(
        @JsonProperty("referencia")
        String referencia,
        @JsonProperty("PIB_municipio")
        Double PIB
) {
}

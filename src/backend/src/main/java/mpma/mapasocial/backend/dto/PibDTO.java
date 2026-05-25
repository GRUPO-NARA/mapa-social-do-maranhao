package mpma.mapasocial.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PibDTO(
        @JsonProperty("referencia")
        String referencia,
        @JsonProperty("PIB_municipio")
        Double PIB
) {
}

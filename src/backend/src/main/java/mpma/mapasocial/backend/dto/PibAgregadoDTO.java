package mpma.mapasocial.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PibAgregadoDTO(
        String referencia,
        @JsonProperty("pib_agregado")
        Double valor
) {
}

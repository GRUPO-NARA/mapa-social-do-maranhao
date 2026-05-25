package mpma.mapasocial.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EvolucaoIDHMunicipalDTO(
        String referencia,
        @JsonProperty("IDH")
        Double IDH
) {
}

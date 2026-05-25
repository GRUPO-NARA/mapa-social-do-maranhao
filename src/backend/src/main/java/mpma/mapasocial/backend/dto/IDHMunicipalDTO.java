package mpma.mapasocial.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IDHMunicipalDTO(
        @JsonProperty("IDH_municipal")
        Double IDH,
        @JsonProperty("referencia")
        String referencia
) {
}

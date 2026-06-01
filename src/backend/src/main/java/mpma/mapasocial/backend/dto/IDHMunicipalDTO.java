package mpma.mapasocial.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para expor o valor do IDH municipal com a referência associada.
 *
 * A propriedade serializada é mapeada para "IDH_municipal" no JSON.
 */
public record IDHMunicipalDTO(
        @JsonProperty("IDH_municipal")
        Double IDH,
        @JsonProperty("referencia")
        String referencia
) {
}
